(ns socializer.table)

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