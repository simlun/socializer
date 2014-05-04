(ns socializer.web.views.seating
  (:use [socializer.web.util :only [response]])
  (:require [net.cgrand.enlive-html :as html]
            [socializer.planner :as planner]
            [socializer.web.views.template :as template]))

(defn set-progress-bar-width
  [percentage]
  (html/set-attr :style (str "width:"
                             percentage
                             "%;")))

(defn set-progress-bar-label
  [percentage]
  (html/content (str percentage "%")))

(defn person-sorted-table-placement
  [event-plan]
  (html/clone-for [placement (sort-by :person-name (:placements event-plan))]
                  [:.person-name] (html/content (:person-name placement))
                  [:.table-name] (html/content (:table-name placement))
                  [:.chair] (html/content (str (:chair placement)))))

(defn table-sorted-table-placement
  [event-plan]
  (let [table-sorted-plan (sort-by :table-name (sort-by :chair (:placements event-plan)))
        tables (distinct (map :table-name table-sorted-plan))]
    (html/clone-for [table tables]
                    [:.table-name] (html/content table)
                    [:.table-group-row] (html/clone-for [placement (filter #(= (:table-name %) table) table-sorted-plan)]
                                                        [:.person-name] (html/content (:person-name placement))
                                                        [:.chair] (html/content (str (:chair placement)))))))

(html/defsnippet plan-snippet "templates/seating-plan.html"
  [:#content]
  [event-plans]
  [:.demo] nil
  [:.event] (html/clone-for [event-plan event-plans]
                            [:.event-date] (html/content (:date (:event event-plan)))
                            [:.event-time] (html/content (:time (:event event-plan)))
                            [:.event-name] (html/content (:name (:event event-plan)))

                            [:.person-sorted-table-placement :.table-placement] (person-sorted-table-placement event-plan)
                            [:.table-grouped-table-placement] (table-sorted-table-placement event-plan)

                            [:.distance-to-reduce.progress-bar] (set-progress-bar-width (-> event-plan :distance :percentage))
                            [:.distance-to-reduce :span] (set-progress-bar-label (-> event-plan :distance :percentage))
                            [:.distance-reduced.progress-bar] (set-progress-bar-width (- 100 (-> event-plan :distance :percentage)))
                            [:.distance-reduced :span] (set-progress-bar-label (- 100 (-> event-plan :distance :percentage)))

                            [:.satisfied-groupings.progress-bar] (set-progress-bar-width (-> event-plan :groupings :percentage-satisfied))
                            [:.satisfied-groupings :span] (set-progress-bar-label (-> event-plan :groupings :percentage-satisfied))
                            [:.dissatisfied-groupings.progress-bar] (set-progress-bar-width (- 100 (-> event-plan :groupings :percentage-satisfied)))
                            [:.dissatisfied-groupings :span] (set-progress-bar-label (- 100 (-> event-plan :groupings :percentage-satisfied)))
                            ))

(defn plan
  [session]
  (response (template/base {:session session
                            :title "Seating Plan"
                            :active-nav "plan"
                            :content (plan-snippet (planner/plan (vals (:events session))
                                                                 (:people session)
                                                                 (:groups session)))})))
