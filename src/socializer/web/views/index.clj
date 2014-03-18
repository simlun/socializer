(ns socializer.web.views.index
  (:use ring.util.response)
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate app-template "templates/app.html"
  [context]
  [:head :title] (html/append (:title context))
  [:.nav :.active] (html/remove-class "active")
  [(keyword (str "#nav-" (:active-nav context)))] (html/add-class "active")
  [:#content] (html/content (:content context)))

(defn index [] (response (app-template {:title "Home"
                                        :active-nav "index"
                                        :content "TODO"})))
