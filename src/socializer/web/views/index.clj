(ns socializer.web.views.index
  (:use ring.util.response)
  (:require [socializer.web.views.template :as template]))

(defn ->index
  [session]
  (response (template/base {:session session
                            :title "Home"
                            :active-nav "index"
                            :content ""})))
