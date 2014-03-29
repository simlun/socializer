(ns socializer.web.views.seating
  (:use [socializer.web.util :only [response]])
  (:require [net.cgrand.enlive-html :as html]
            [socializer.web.views.template :as template]))

(html/defsnippet plan-snippet "templates/seating-plan.html"
  [:#content]
  []
  )

(defn plan
  [session]
  (response (template/base {:session session
                            :title "Seating Plan"
                            :active-nav "plan"
                            :content (plan-snippet)})))

