(ns socializer.web.views
  (:use ring.util.response)
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate app-template "templates/app.html"
  [context]
  [:head :title] (html/append (:title context))
  [:.nav :.active] (html/remove-class "active")
  [(keyword (str "#nav-" (:active-nav context)))] (html/add-class "active")
  [:#content] (html/content (:content context)))

(html/defsnippet people-form "templates/people-form.html"
  [:body]
  [people]
  ; TODO List people separated by linebreaks
  ; TODO Sort people alphabetically
  [:#people] (html/content (str people)))

(defn index [] (response (app-template {:title "Home"
                                        :active-nav "index"
                                        :content "TODO"})))

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/split people #"\n")
        people (set people)]
    people))

(defn store-people [session params]
  (let [people (parse-set-of-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session {:people people}))))

(defn list-people [session]
  (response (app-template {:title "People"
                           :active-nav "people"
                           :content (people-form (:people session))})))
