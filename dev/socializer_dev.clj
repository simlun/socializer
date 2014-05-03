(ns socializer-dev
  (:use simlun.enduro-session)
  (:require [socializer.web.routes :as routes]))

(def dev-handler
  (-> routes/base-handler
      wrap-enduro-session))
