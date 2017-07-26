<!-- 
@date           04/01/2015
@Author         Saima Aziz
@Description    A child view to show a SINGLE ROUTE on the map
                This Page tracks realtime location of the employee and cab on the routes. It gives further details of ETA and more. It has nested view of Map.
@State          home.viewmap >> home.viewmap.showAll
@URL            /viewmap/showRoutes   

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
05/01/2015  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->

<efmfm-google-map-single id = 'map-canvas1' map-data = "getSingleMap()"></efmfm-google-map-single>
