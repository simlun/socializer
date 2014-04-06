(ns socializer.planner
  (:use [clojure.math.combinatorics :only [cartesian-product]])
  (:require [clojure.math.numeric-tower :as math]))

; TODO Refactor into table.clj and distance.clj

(defn to-table-chair-map
  [[table-name table]]
  (map #(hash-map :table-name %1 :table-shape %2 :chair %3)
       (repeat table-name)
       (repeat (:shape table))
       (range (:nr-chairs table))))

(defn table-placement
  [participants tables order-fn]
  (let [sorted-participants (order-fn participants)
        sorted-tables (into (sorted-map) tables)
        person-name-maps (map #(hash-map :person-name %)
                              sorted-participants)
        table-chair-maps (apply concat (map to-table-chair-map
                                            sorted-tables))]
    (vec (map merge
              person-name-maps
              table-chair-maps))))

(defn sorted-table-placement
  [participants tables]
  (table-placement participants tables sort))

(defn random-table-placement
  [participants tables]
  (table-placement participants tables shuffle))

(def placement-functions
  {:sorted sorted-table-placement
   :random random-table-placement})

(defn create-matrix
  ([people]
   (create-matrix people 1000))
  ([people value]
   (zipmap (clojure.set/difference
             (set (map set (cartesian-product people people)))
             (set (map hash-set people)))
           (repeat value))))

(defn judge-table
  [[table-name table] placements]
  (let [table-placements (filter #(= (:table-name %) table-name) placements)
        neighbours (set (map :person-name table-placements))]
    (create-matrix neighbours 0)))

(defn judge-room
  [tables placements]
  (apply merge (map #(judge-table % placements) tables)))

(def distance-judge-functions
  {:chair-agnostic judge-room})

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
  (let [people-distance-matrix (create-matrix people)
        max-distance (matrix-sum people-distance-matrix)]
    (loop [planned-events []
           unplanned-events (sorted-events events)
           previous-distance-matrix people-distance-matrix]
      (if (empty? unplanned-events)
        planned-events
        (let [event (first unplanned-events)
              placement-fn ((:placement-algorithm event) placement-functions)
              placements (placement-fn (:participants event)
                                       (:tables event))
              distance-judge-fn ((:distance-algorithm event) distance-judge-functions)
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
