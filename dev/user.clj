(ns user
  (:require [babashka.deps :as deps]
            [clojure.edn :as edn])
  (:import (java.time Instant))

(defn log [msg data]
  (prn {:msg msg
        :data data
        :timestamp (str (Instant/now))}))

(defn error [msg data]
  (log msg data)
  (throw (ex-info msg data)))

(defn validate-aws-response [res]
  (when (:cognitect.anomalies/category res)
    (let [data (merge (select-keys res [:cognitect.anomalies/category])
                      {:err-msg (:Message res)
                       :err-type (:__type res)})]
      (error "AWS request failed due to " data)))
  res)

(defn refresh-deps []
  (-> (slurp "bb.edn")
      edn/read-string
      deps/add-deps))


(comment
(refresh-deps)
  ,)
