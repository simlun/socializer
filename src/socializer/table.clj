(ns socializer.table
  (:require [socializer.distance :as distance]))

(defn to-table-chair-map
  [[table-name table]]
  (map #(hash-map :table-name %1 :chair %2)
       (repeat table-name)
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
  [participants tables _]
  (table-placement participants tables sort))

(defn random-table-placement
  [participants tables _]
  (table-placement participants tables shuffle))

(defn greedy-table-placement
  [participants tables previous-distance-matrix]
  (let [distance-sort (fn [people] (distance/people-sorted-by-distance people previous-distance-matrix))]
    (table-placement participants tables distance-sort)))

(def placement-functions
  {:sorted sorted-table-placement
   :random random-table-placement
   :greedy greedy-table-placement})
