(ns socializer.web.views.people
  (:use [socializer.web.util :only [response]]
        [ring.util.response :only [redirect-after-post]])
  (:require [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/people-form.html"
  [:#content]
  [people]
  [:#people] (html/content (clojure.string/join "\n"
                                                (sort people))))

(defn parse-people [params]
  ; TODO Take people instead of params as input?
  (let [{people :people} params]
    (->> people
         (clojure.string/trim)
         (clojure.string/split-lines)
         (map clojure.string/trim)
         (filter (complement empty?))
         (set))))

(defn ->store [session params]
  (let [people (parse-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session session)
        (assoc-in [:session :people] people))))

(defn ->form [session]
  (response (template/base {:session session
                            :title "People"
                            :active-nav "people"
                            :content (form-snippet (:people session))})))
