(ns socializer.web.views.data
  (:use [socializer.web.util :only [response]]
        clojure.pprint)
  (:require [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/data-form.html"
  [:#content]
  [data]
  [:#data] (html/content (with-out-str (pprint data))))

(defn form [session]
  (response (template/base {:session session
                            :title "Data"
                            :active-nav ""
                            :content (form-snippet session)})))
