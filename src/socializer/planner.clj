(ns socializer.planner
  (:use [clojure.math.combinatorics :only [cartesian-product]])
  (:require [clojure.math.numeric-tower :as math]))

(defn- percentage
  [part whole]
  (int (* 100 (float (/ part whole)))))

(defn to-table-chair-map
  [[table-name table]]
  (map #(hash-map :table-name %1 :chair %2)
       (repeat table-name)
       (range (:nr-chairs table))))

(defn linear-table-placement
  [participants tables]
  (let [sorted-participants (sort participants)
        person-name-maps (map #(hash-map :person-name %)
                              sorted-participants)
        table-chair-maps (apply concat (map to-table-chair-map tables))]
    (vec (map merge
              person-name-maps
              table-chair-maps))))

(defn linear-plan-1
  [event max-distance]
  (let [placements (linear-table-placement (:participants event)
                                           (:tables event))
        previous-distance 225000 ; TODO
        distance-delta 102000 ; TODO
        current-distance (- previous-distance distance-delta)
        distance-percentage (percentage current-distance max-distance)]
    {:event {:date (:date event)
             :time (:time event)
             :name (:event-name event)}
     :distance {:max max-distance
                :current 123000 ; TODO
                :percentage distance-percentage}
     :placements placements}))


(defn create-matrix
  [people]
  (zipmap (clojure.set/difference
            (set (map set (cartesian-product people people)))
            (set (map hash-set people)))
          (repeat 1000)))

(defn linear-plan
  [events people]
  (let [sorted-events (->> events
                           (sort-by :time)
                           (sort-by :date))
        max-distance (* 1000 (math/expt (count people)
                                        2))]
    (loop [planned-events []
           unplanned-events sorted-events]
      (if (empty? unplanned-events)
        planned-events
        (recur (conj planned-events
                     (linear-plan-1 (first unplanned-events) max-distance))
               (rest unplanned-events))))))
