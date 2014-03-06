(ns datomic-test.core
  (:require [datomic.api :as d]))


;; db connection
(def uri "datomic:free://localhost:4334/users-dbtest")
(def conn (d/connect uri))

;; helper functions
(defn account-user [user-name]
  (ffirst (d/q '[:find ?eid
                 :in $ ?user-name
                 :where [?eid :user/name ?user-name]]
               (d/db conn)
               user-name)))

;; query functions
(defn add-user [user-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :user/name user-name}]))

(defn add-account [account-name user-name]
  (let [account-id (d/tempid :db.part/user)]
    @(d/transact conn [{:db/id account-id
                        :account/name account-name}
                       {:db/id (account-user user-name)
                        :user/accounts account-id}])))

(defn all-users []
  (d/q '[:find ?user-name
         :where [_ :user/name ?user-name]]
       (d/db conn)))

(defn user-accounts [user-name]
  (d/q '[:find ?account-name
         :in $ ?user-name
         :where [?eid :user/name ?user-name]
                [?eid :user/accounts ?account]
                [?account :account/name ?account-name]]
       (d/db conn)
       user-name))

;; ----- USAGE EXAMPLES -----
;; (add-user "Bob")
;; (all-users)
;; (add-account "Bob-1" "Bob")
;; (user-accounts "Bob")
