(ns socializer.web.views.index
  (:use [socializer.web.util :only [response]])
  (:require [socializer.web.views.template :as template]))

(defn ->index
  [session]
  (response (template/base {:session session
                            :title "Home"
                            :active-nav "index"
                            :content ""})))
