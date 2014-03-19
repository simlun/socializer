(ns socializer.web.views.index
  (:use ring.util.response
        ring.util.codec)
  (:require [net.cgrand.enlive-html :as html]))


(html/deftemplate app-template "templates/app.html"
  [context]

  [:head :title] (html/append (:title context))

  [:.nav :.active] (html/remove-class "active")
  [(keyword (str "#nav-" (:active-nav context)))] (html/add-class "active")

  [:.groups :.group]
  (html/clone-for [[group-name group] (:groups (:session context))]
                  [:.name] (html/content group-name)
                  [:a] (html/set-attr :href (str "/groups/"
                                                 (url-encode group-name)))
                  [:.count] (html/content (str (count (:members group)))))

  [:#content] (html/substitute (:content context)))


(defn ->index
  [session]
  (response (app-template {:session session
                           :title "Home"
                           :active-nav "index"
                           :content ""})))
