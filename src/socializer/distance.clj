(ns socializer.distance
  (:use clojure.set
        [clojure.math.combinatorics :only [cartesian-product]]))

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

(def judge-functions
  {:chair-agnostic judge-room})