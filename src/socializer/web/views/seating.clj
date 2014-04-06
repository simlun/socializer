(ns socializer.web.views.seating
  (:use [socializer.web.util :only [response]])
  (:require [net.cgrand.enlive-html :as html]
            [socializer.planner :as planner]
            [socializer.web.views.template :as template]))

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
                                                                [:.chair] (html/content (str (:chair placement))))

                            [:.distance-left.progress-bar] (html/set-attr :style (str "width:"
                                                                                           (:percentage (:distance event-plan))
                                                                                           "%;"))
                            [:.distance-left :span] (html/content (str (:percentage (:distance event-plan)) "%"))
                            [:.distance-reduced.progress-bar] (html/set-attr :style (str "width:"
                                                                                           (- 100 (:percentage (:distance event-plan)))
                                                                                           "%;"))
                            [:.distance-reduced :span] (html/content (str (- 100 (:percentage (:distance event-plan))) "%"))

                            ))

(defn plan
  [session]
  (response (template/base {:session session
                            :title "Seating Plan"
                            :active-nav "plan"
                            :content (plan-snippet (planner/linear-plan (vals (:events session))
                                                                        (:people session)))})))
