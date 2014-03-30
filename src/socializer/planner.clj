(ns socializer.planner)

(defn linear-table-placement
  [participants tables]
  (let [sorted-participants (sort participants)
        person-name-maps (map #(hash-map :person-name %)
                              sorted-participants)
        room-to-map (fn [[table-name table]]
                      (map #(hash-map :table-name %1 :chair %2)
                           (repeat table-name)
                           (range (:nr-chairs table))))
        table-chair-maps (apply concat (map room-to-map tables))]
    (println person-name-maps)
    (println table-chair-maps)
    (vec (map merge
              person-name-maps
              table-chair-maps))))

(defn linear-plan-1
  [event]
  {:event {:date (:date event)
           :time (:time event)
           :name (:event-name event)}
   :placements (linear-table-placement (:participants event)
                                       (:tables event))})

(defn linear-plan
  [events]
  (map linear-plan-1 events))
