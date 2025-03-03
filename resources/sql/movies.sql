-- :name create-movies! :! :n
-- :doc creates movies by ship name
INSERT INTO movies
(ship_name, movie_name)
VALUES (:ship_name, :movie_name)


-- :name get-movies :?
-- :doc retrieves a user record given the id
SELECT * FROM movies
WHERE ship_name = :ship_name