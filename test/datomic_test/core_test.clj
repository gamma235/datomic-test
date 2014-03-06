(ns datomic-test.core-test
  (:require [expectations :refer :all]
            [datomic-test.core :refer :all]
            [datomic.api :as d]))


(defn clear-db []
  (let [uri "datomic:mem://users-dbtest"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema)
      conn)))

;; adding one user should let us find just one
(expect #{["John"]}
        (with-redefs [conn (clear-db)]
        (do
          (add-user "John")
          (all-users))))

;; adding multiple users should allow us to find all those users
(expect #{["John"] ["Paul"] ["George"]}
        (with-redefs [conn (clear-db)]
          (do
            (add-user "John")
            (add-user "Paul")
            (add-user "George")
            (all-users))))

;; adding one user with one account should allow us to find that name for that account
(expect #{["account-1"]}
        (with-redefs [conn (clear-db)]
          (do
            (add-user "John")
            (add-account "account-1" "John")
            (user-accounts "John"))))

;; adding multiple users and accounts, should allow us to find accounts for a particular user
(expect #{["account-3"] ["account-4"]}
        (with-redefs [conn (clear-db)]
          (do
            (add-user "John")
            (add-account "account-1" "John")
            (add-account "account-2" "John")
            (add-user "Paul")
            (add-account "account-3" "Paul")
            (add-account "account-4" "Paul")
            (user-accounts "Paul"))))
