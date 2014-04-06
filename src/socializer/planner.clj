(ns socializer.planner
  (:require [socializer.table :as table]
            [socializer.distance :as distance]))

(defn- matrix-sum
  [matrix]
  (reduce + (vals matrix)))

(defn- percentage
  [part whole]
  (int (* 100 (float (/ part whole)))))

(defn create-planned-event
  [event
   placements
   current-distance-matrix
   max-distance]
  (let [current-distance (matrix-sum current-distance-matrix)
        distance-percentage (percentage current-distance max-distance)]
    {:event {:date (:date event)
             :time (:time event)
             :name (:event-name event)}
     :distance {:max max-distance
                :current current-distance
                :percentage distance-percentage}
     :placements placements}))

(defn- sorted-events
  [events]
  (->> events
       (sort-by :time)
       (sort-by :date)))

(defn plan
  [events people]
  (let [people-distance-matrix (distance/create-matrix people)
        max-distance (matrix-sum people-distance-matrix)]
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
              planned-event (create-planned-event event
                                                  placements
                                                  current-distance-matrix
                                                  max-distance)]
          (recur (conj planned-events planned-event)
                 (rest unplanned-events)
                 current-distance-matrix))))))
