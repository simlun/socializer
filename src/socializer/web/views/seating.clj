(ns socializer.web.views.seating
  (:use [socializer.web.util :only [response]])
  (:require [net.cgrand.enlive-html :as html]
            [socializer.planner :as planner]
            [socializer.web.views.template :as template]))

(def plan-stub
  [{:event {:date "2014-02-25"
            :time "12:00"
            :name "Lunch"}
    :placements [{:person-name "Dave"
                  :table-name "A"
                  :chair 0}
                 {:person-name "Charlotte"
                  :table-name "B"
                  :chair 2}
                 {:person-name "Bob"
                  :table-name "A"
                  :chair 1}
                 {:person-name "Alice"
                  :table-name "B"
                  :chair 0}
                 {:person-name "Erin"
                  :table-name "A"
                  :chair 1}
                 ]}
   {:event {:date "2014-02-25"
            :time "18:00"
            :name "Dinner"}
    :placements [{:person-name "Bob"
                  :table-name "B"
                  :chair 2}
                 {:person-name "Alice"
                  :table-name "A"
                  :chair 0}
                 {:person-name "Erin"
                  :table-name "A"
                  :chair 1}
                 {:person-name "Dave"
                  :table-name "B"
                  :chair 1}
                 {:person-name "Charlotte"
                  :table-name "A"
                  :chair 0}
                 ]}
   ])

(html/defsnippet plan-snippet "templates/seating-plan.html"
  [:#content]
  [event-plans]
  [:.demo] nil
  [:.event] (html/clone-for [event-plan event-plans]
                            [:.event-date] (html/content (:date (:event event-plan)))
                            [:.event-time] (html/content (:time (:event event-plan)))
                            [:.event-name] (html/content (:name (:event event-plan)))

                            [:.person-sorted-table-placement
                             :.table-placement] (html/clone-for [placement (:placements event-plan)]
                                                                [:.person-name] (html/content (:person-name placement))
                                                                [:.table-name] (html/content (:table-name placement))
                                                                [:.chair] (html/content (str (:chair placement))))))

(defn plan
  [session]
  (response (template/base {:session session
                            :title "Seating Plan"
                            :active-nav "plan"
                            :content (plan-snippet (planner/linear-plan (vals (:events session))))})))
