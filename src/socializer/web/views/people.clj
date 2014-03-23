(ns socializer.web.views.people
  (:use ring.util.response)
  (:require [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/people-form.html"
  [:#content]
  [people]
  [:#people] (html/content (clojure.string/join "\n"
                             (sort people))))

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/trim people)
        people (clojure.string/split people #"\n")
        people (map clojure.string/trim people)
        people (filter (complement empty?) people)
        people (set people)]
    people))

(defn ->store [session params]
  (let [people (parse-set-of-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session session)
        (assoc-in [:session :people] people))))

(defn ->form [session]
  (response (template/base {:session session
                            :title "People"
                            :active-nav "people"
                            :content (form-snippet (:people session))})))
