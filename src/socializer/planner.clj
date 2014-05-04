(ns socializer.planner
  (:require [socializer.table :as table]
            [socializer.distance :as distance]))

(defn- matrix-sum
  [matrix]
  (reduce + (vals matrix)))

(defn- percentage
  [part whole]
  (int (* 100 (float (/ part whole)))))

(defn- flatten-1-level
  [nested-things]
  (mapcat identity nested-things))

(defn create-group-member-pairs
  [groups]
  (->> (vals groups)
       (map :members)
       (map distance/create-pairs)
       flatten-1-level))

(defn create-person-name-set-per-table
  "Create a set of person names per table in the placements"
  [event-placement]
  (->> event-placement
       (group-by :table-name)
       vals
       (map #(map :person-name %))
       (map set)))

(defn count-satisfied-groupings
  [placements groups]
  (let [person-placements (create-person-name-set-per-table placements)
        subset-of-table? (fn [pair] (map (partial clojure.set/subset? pair)
                                         person-placements))]
    (->> (create-group-member-pairs groups)
         (map subset-of-table?)
         (map (partial every? false?))
         (filter true?)
         count)))

(defn create-planned-event
  [event
   placements
   current-distance-matrix
   max-distance
   groups
   counted-group-pair-total]
  (let [current-distance (matrix-sum current-distance-matrix)
        satisfied-groupings (count-satisfied-groupings placements groups)]
    {:event {:date (:date event)
             :time (:time event)
             :name (:event-name event)}
     :distance {:max max-distance
                :current current-distance
                :percentage (percentage current-distance max-distance)}
     :groupings {:pairwise-total counted-group-pair-total
                 :pairwise-satisfied satisfied-groupings
                 :percentage-satisfied (percentage satisfied-groupings counted-group-pair-total)}
     :placements placements}))

(defn- sorted-events
  [events]
  (->> events
       (sort-by :time)
       (sort-by :date)))

(defn grouped-pair-count
  [groups]
  (count (create-group-member-pairs groups)))

(defn plan
  [events people groups]
  (let [people-distance-matrix (distance/create-matrix people)
        max-distance (matrix-sum people-distance-matrix)
        counted-group-pair-total (grouped-pair-count groups)]
    (loop [planned-events []
           unplanned-events (sorted-events events)
           previous-distance-matrix people-distance-matrix]
      (if (empty? unplanned-events)
        planned-events
        (let [event (first unplanned-events)
              placement-fn ((:placement-algorithm event) table/placement-functions)
              placements (placement-fn (:participants event)
                                       (:tables event))
              distance-judge-fn ((:distance-algorithm event) distance/judge-functions)
              placement-distance-matrix (distance-judge-fn (:tables event)
                                                           placements)
              current-distance-matrix (merge previous-distance-matrix
                                             placement-distance-matrix)

              ; TODO Refactor create-planned-event to a merge of :event,
              ;      :distance, ... values into separate functions?
              planned-event (create-planned-event event
                                                  placements
                                                  current-distance-matrix
                                                  max-distance
                                                  groups
                                                  counted-group-pair-total)]
          (recur (conj planned-events planned-event)
                 (rest unplanned-events)
                 current-distance-matrix))))))
