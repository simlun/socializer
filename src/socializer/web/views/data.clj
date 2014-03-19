(ns socializer.web.views.data
  (:use clojure.pprint
        ring.util.response
        [socializer.web.views.index :only [app-template]])
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/data-form.html"
  [:#content]
  [data]
  [:#data] (html/content (with-out-str (pprint data))))

(defn ->form [session]
  (response (app-template {:session session
                           :title "Data"
                           :active-nav ""
                           :content (form-snippet session)})))
