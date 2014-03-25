(ns socializer.web.views.template
  (:use ring.util.codec)
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate base "templates/base.html"
  [context]

  [:head :title] (html/append (:title context))

  [:.nav :.active] (html/remove-class "active")
  [(keyword (str "#nav-" (:active-nav context)))] (html/add-class "active")

  [:.groups :.group]
    (html/clone-for [[group-name group] (:groups (:session context))]
                    [:a] (html/set-attr :href (str "/groups/"
                                                   (url-encode group-name)))
                    [:.name] (html/content group-name)
                    [:.count] (html/content (str (count (:members group)))))

  [:.events :.event]
    (html/clone-for [[event-key event] (:events (:session context))]
                    [:a] (html/set-attr :href (str "/events/"
                                                   (url-encode event-key)))
                    [:.name] (html/content event-key)
                    [:.count] (html/content (str (count (:participants event)))))

  [:#content] (html/substitute (:content context)))

