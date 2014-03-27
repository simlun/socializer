(ns socializer.web.views.groups
  (:use [socializer.web.util :only [response]]
        [ring.util.response :only [redirect-after-post]]
        [socializer.web.views.people :only [parse-people]])
  (:require [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))


(html/defsnippet form-snippet "templates/group-form.html"
  [:#content]
  [group-name group]

  [:#group-name] (html/set-attr :value group-name)

  [:select (html/attr= :value (:intention group))]
  (html/set-attr :selected "selected")

  [:#people] (html/content (clojure.string/join "\n" (sort (:members group)))))


(defn form
  ([session]
   (response (template/base {:session session
                             :title "Add Group"
                             :active-nav "groups"
                             :content (form-snippet "" {})})))
  ([session group-name]
   (response (template/base {:session session
                             :title "Edit Group"
                             :active-nav "groups"
                             :content (form-snippet
                                        group-name
                                        (get (:groups session) group-name))}))))


(defn store [session params]
  (let [group-name (:group-name params)
        group {:intention (:intention params)
               :members (parse-people params)}]
    (-> (redirect-after-post "/groups")
        (assoc :session session)
        (assoc-in [:session :groups group-name] group))))
