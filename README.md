Socializer
==========

Definitions
-----------

- People/Person
- Groups/Group
- Events/Event
- Participations/Participation
- Participants
- Rooms/Room
- Tables/Table
- Chairs/Chair
- Distances/Distance
- Placements/Placement

- Social distance


Summary
-------

- A minimization problem
- Minimize the total `social distance` between all pairs of people, accumulated over all participations.


Todo's
------

- Different people attending different events (participation)
- Grouping of people (affects seating)
- Different table types:
    - Classic rectangular 2-sided
    - Rectangular 4-sided
    - Circular small/large
    - Etc...
- More tables than people
- Less tables than people
- Assymmetrical number of chairs at rectangular tables

Thoughts
--------

- Tables have positions too
- How to represent chair positions at tables?
    - Coordinates
    - Angle from middle of table
    - Enumeration from 12:00, clockwise
    - Rectangular:
        - Nr of chairs wide
        - Nr of chairs deep
    - Precalculated in the graph? The chairs at the tables are complete graphs?
- Can social distance increase? No?
- I can do a full search for the minimum total social distance for each seating?
- Real time analysis for manually switching seats with people.


Inputs
------

- People
    - Associated with 0 or more Groups
    - Associated with 0 or more Events
- Events
    - Associated with 1 Room
    - Associated with 1 or more unique Tables
- Tables
    - Associated with 1 or more unique Chairs


### Data

    ;; People
    [
    {:name "Alice"}
    {:name "Bob"}
    {:name "Cathrin"}
    {:name "Dave"}
    {:name "Erin"}
    {:name "Fred"}
    ]

    ;; Events
    [
    {:name "Dinner", :datetime "2014-05-15 18:00, :tables [{:name "A", :type :rectangular, :nr-chairs 6}]}
    {:name "Lunch", :datetime "2014-05-16 12:00", :tables [{:name "A", :type :rectangular, :nr-chairs 2}
                                                           {:name "B", :type :rectangular, :nr-chairs 2}
                                                           {:name "C", :type :rectangular, :nr-chairs 2}]}
    ]



    ;; Starting social distances
    [
    {:pair #{"Alice" "Bob"}, :distance 1000}
    {:pair #{"Alice" "Cathrin"}, :distance 1000}
    {:pair #{"Alice" "Dave"}, :distance 1000}
    {:pair #{"Alice" "Erin"}, :distance 1000}
    {:pair #{"Alice" "Fred"}, :distance 1000}
    {:pair #{"Bob" "Cathrin"}, :distance 1000}
    {:pair #{"Bob" "Dave"}, :distance 1000}
    {:pair #{"Bob" "Erin"}, :distance 1000}
    {:pair #{"Bob" "Fred"}, :distance 1000}
    ; etc ...
    ]


Outputs
-------

- Distances
    - Associated with two People and an Event
- Seatings
    - Associated with a Person, Event and a Chair

### Example

    ;; Dinner
 
    ; - First table
    +--+--+--+
    |  |  |  |
    +--+--+--+
    |  |  |  |
    +--+--+--+
    ; - Find chair with best opportunity for decreasing total social distance
    ;   ...or any if no ppl are at the table?
    ; - Place person with highest distance to everyone else => Alice
    +--+--+-------+
    |  |  | Alice |
    +--+--+-------+
    |  |  |       |
    +--+--+-------+
    ; - Find chair with best opportunity for decreasing total social distance
    ; - ...the one with the lowest new social distance if someone sits there.
    ; - Place person for which the total social distance becomes the lowest => Fred
    +--+--+-------+
    |  |  | Alice |
    +--+--+-------+
    |  |  | Fred  |
    +--+--+-------+
    ; - Find chair with best opportunity for decreasing total social distance => One of the middle chair
    ; - Find person with highest distance to Alice and Fred and place at the chair => Bob
    ;   ...see above.
    +--+-----+-------+
    |  | Bob | Alice |
    +--+-----+-------+
    |  |     | Fred  |
    +--+-----+-------+
    ; ...
    +---------+------+-------+
    | Cathrin | Bob  | Alice |
    +---------+------+-------+
    | Dave    | Erin | Fred  |
    +---------+------+-------+

    [
    {:pair #{"Alice" "Bob"}, :distance 1}
    {:pair #{"Alice" "Cathrin"}, :distance 4}
    {:pair #{"Alice" "Dave"}, :distance 2}
    {:pair #{"Alice" "Erin"}, :distance 1}
    {:pair #{"Alice" "Fred"}, :distance 0}
    {:pair #{"Bob" "Cathrin"}, :distance 1}
    {:pair #{"Bob" "Dave"}, :distance 1}
    {:pair #{"Bob" "Erin"}, :distance 0}
    {:pair #{"Bob" "Fred"}, :distance 1}
    {:pair #{"Cathrin" "Dave"}, :distance 0}
    {:pair #{"Cathrin" "Erin"}, :distance 1}
    {:pair #{"Cathrin" "Fred"}, :distance 2}
    {:pair #{"Dave" "Erin"}, :distance 1}
    {:pair #{"Dave" "Fred"}, :distance 4}
    {:pair #{"Erin" "Fred"}, :distance 1}
    ]


    ;; Lunch
    
    ; - First table
    ; - Place person with highest distance to someone else => Alice
    ; - Find person with highest distance to Alice and place at the optimal position => Cathrin
    +---------+
    | Alice   |
    +---------+
    | Cathrin |
    +---------+

    ; - Second table
    ; - Place person with highest distance to someone else => Dave
    ; - Find person with highest distance to Dave and place at the optimal position => Fred
    +------+
    | Dave |
    +------+
    | Fred |
    +------+
     
    ; - Third table
    ; - Place person with highest distance to someone else => Bob
    ; - Find person with highest distance to Bob and place at the optimal position => Erin
    +------+
    | Bob  |
    +------+
    | Erin |
    +------+
    
    [
    {:pair #{"Alice" "Bob"}, :distance 1}
    {:pair #{"Alice" "Cathrin"}, :distance 0}
    {:pair #{"Alice" "Dave"}, :distance 2}
    {:pair #{"Alice" "Erin"}, :distance 1}
    {:pair #{"Alice" "Fred"}, :distance 0}
    {:pair #{"Bob" "Cathrin"}, :distance 1}
    {:pair #{"Bob" "Dave"}, :distance 1}
    {:pair #{"Bob" "Erin"}, :distance 0} ;; <-- still 0...
    {:pair #{"Bob" "Fred"}, :distance 1}
    {:pair #{"Cathrin" "Dave"}, :distance 0}
    {:pair #{"Cathrin" "Erin"}, :distance 1}
    {:pair #{"Cathrin" "Fred"}, :distance 2}
    {:pair #{"Dave" "Erin"}, :distance 1}
    {:pair #{"Dave" "Fred"}, :distance 0}
    {:pair #{"Erin" "Fred"}, :distance 1}
    ]


### Data

    [
    {:name "A", :type :rectangular, :placements [{:name "Alice"}
                                                 {:name "Erin"}]}
    {:name "B", :type :rectangular, :placements [{:name "Fred"}
                                                 {:name "Dave"}
                                                 {:name "Bob"}]}
    ; etc...
    ]

    [
    {:person-name "Alice", :table-name "A", :chair-nr 0}
    {:person-name "Bob", :table-name "B", :chair-nr 2}
    {:person-name "Dave", :table-name "B", :chair-nr 1}
    {:person-name "Erin", :table-name "A", :chair-nr 1}
    {:person-name "Fred", :table-name "B", :chair-nr 0}
    ; etc...
    ]


Table Models
------------

### Rectangular

    def dist(x, y):
        if y == 0:
            return x**2
        else:
            return x * y

Ex:

      -2   -1    0    1    2    3    4       x ->
    +----+----+----+----+----+----+----+
    |  2 |  1 |  0 |  1 |  2 |  3 |  4 |  1
    +----+----+----+----+----+----+----+
    |  4 |  1 |    |  1 |  4 |  9 | 16 |  0
    +----+----+----+----+----+----+----+
                ^^                        ^
               Origo                      |
                                          y



User Stories
============

- Ellinor creates a list of people in a text area of names separated by line breaks.
- Ellinor creates an event giving it a name, date, line break separated list of names of guests and a similar list of tables. 
- (The list of tables are on the form of `<table name>: <table type>: <nr of chairs>`.)

- The tables are entered as a 2D matrix in a text field.

<pre>
A B C
D - E
- F -
</pre>

- Each table type have a predefined chair numbering. Chairs are placed at rectangular tables like houses on a street. (Even on one side, odd on the other). Round just around the table.

- Ellinor groups the people by entering a group name and the names of the members. (Groups that should not sit with each other by the force of a given weight).

- Now Ellinor just wants the seating list as output.
    - Shown per event
    - Primarily a list of people at numbered chairs at named tables, sorted by person names
    - Also an interactive graphical representation.

- Ellinor identifies two people which should not sit together. Selecting one person in the GUI shows a heat map of the effect of swapping the selected person with all the other people.


Pseudocode v1
-------------

    # All people start with infinite distances between each other

    for event in events:
        room = event.room
        for table in room.tables:
            while table.has_empty_chairs():
                person = event.unseated_person_with_highest_total_social_distance()
                seat_at_table(person, table)


    def seat_at_table(person, table):
        complete_new_distance_matrix = all_possible_new_social_distances(person, table)
        chair = chair_with_min_new_distance(complete_new_distance_matrix)
        seat_at_chair(person, chair)


Pseudocode v2
-------------

<pre>


;; Socializer


(def people #{"Alice" "Bob" "Cathrin" "Dave"})

;; TODO (defn generate-initial-distances [people])
;; TODO (def initial-distances (generate-initial-distances people))
(def initial-distances {
  #{"Alice" "Bob"} 1000
  #{"Alice" "Cathrin"} 1000
  #{"Alice" "Dave"} 1000
  #{"Bob" "Cathrin"} 1000
  #{"Bob" "Dave"} 1000
  #{"Cathrin" "Dave"} 1000
  })

(defn place-1 [people chairs distances]
  (let [max-asocial-person (person-with-max-distance-sum people distances)
        optimally-social-chair (...)]
    {:person max-asocial-person
     :chair optimally-social-chair}))

(defn place-all [people chairs distances]
  
  )

(defn place [people chairs]

; ...events?!









</pre>





















