Ideas
=====

* Walking Skeleton
* ATDD
* TDD
* Deploy to Heroku
* Deploy as .deb
* Clojure is fun

* People/Person
* Groups/Group: a set of People
* Events/Event: with a date, time, Room and a list of People
* Participants/Participant: Person attending an Event is a Participant

* Positions/Position: a location in a Room (x, y and rotation)
* Rooms/Room: a Room is a specific set of Tables with Positions
* Tables/Table: tables have a Shape and Chairs
* Shapes/Shape: tables have different Shapes
* Chairs/Chair: a place to sit at a Table
* Venues/Venue

* Person == Guest?
* List of People == Guest List?

* Bulk edit all the things

* Associate special food needs with people to help waiters (show in printed special table seating map) and for dinner planning
* Keep track of Venue max allowed people

* Support last-minute changes
* Swap places

* Rooms could be shared on the site. Resturants could manage their own rooms for example and let customers use them.

* Both automatic and manual placements of People at Tables
* Attract some groups of People
* Repel some groups of People


Stories
=======

* Create an automatic arbitrary Table placement for a few People at a few Tables for a single Event
* Create an automatic arbitrary Table placement for a few People at a few Tables for multiple Events

* Print table seating lists
    * Sorted by Person names
    * Sorted by Table and Chair numbers

* Select People placement algorithm
    * Place People in arbitrary order
    * Place People in random order
    * Place People in a "good" order based on the People they have been sitting with at previous Events
        * Select memory depth (forget Events log time ago)

* Store all state for signed in users

* Print graphical table seating maps
    * Graphically set table positions and rotations


Workflows
=========

* First screen is a landing page
    * Create new Event
    * Sign in to continue with your previous events

* Create new Event
    * Enter a list of People (or use existing list)
    * Enter Room information (number of Tables, Table types, Table sizes, Table Positions) (or use existing Room)
    * Metadata: date, time, address, etc.
