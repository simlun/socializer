(ns socializer.web.views.data
  (:use [socializer.web.util :only [response]]
        [ring.util.response :only [redirect-after-post]]
        clojure.pprint)
  (:require clojure.edn
            [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/data-form.html"
  [:#content]
  [data]
  [:#data] (html/content (with-out-str (pprint data))))

(defn form [session]
  (response (template/base {:session session
                            :title "Data"
                            :active-nav "data"
                            :content (form-snippet session)})))

(defn store [session params]
  (let [data-str (:data params)
        data-edn (clojure.edn/read-string data-str)]
    (-> (redirect-after-post "/data")
        (assoc :session data-edn))))
