(ns socializer.distance
  (:use clojure.set
        [clojure.math.combinatorics :only [cartesian-product]]))

(defn create-pairs
  [things]
  (seq (clojure.set/difference
         (set (map set (cartesian-product things things)))
         (set (map hash-set things)))))

(defn create-matrix
  ([people]
   (create-matrix people 1000))
  ([people value]
   (zipmap (create-pairs people)
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

(defn- if-set-convert-to-vec
  [thing]
  (if (set? thing)
    (into [] thing)
    thing))

(defn sorted-map-by-values
  "Inspiration: http://clojuredocs.org/clojure_core/clojure.core/sorted-map-by#example_836"
  [unsorted-map]
  (into (sorted-map-by (fn [key1 key2]
                         (compare [(get unsorted-map key2) (if-set-convert-to-vec key2)]
                                  [(get unsorted-map key1) (if-set-convert-to-vec key1)])))
        unsorted-map))

(defn people-sorted-by-distance
  "Sorted so people with the largest distance values are first"
  [people matrix]
  (let [sorted-matrix (sorted-map-by-values matrix)]
    (filter people
            (distinct (flatten (map #(into [] %)
                                    (into [] (keys sorted-matrix))))))))
