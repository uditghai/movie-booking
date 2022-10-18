Describe transactional scenarios and design decisions to address the same.

* The following scenarios exist in the application
    * Admin activities like creation of Partners, Shows, Movies, Theatres
        * these actions though critical are primarily either batched operations or handled by the
          internal team with not much concurrency related concerns. these can be handled by a
          typical data repository / controller combination. multiple operations in a single actions
          can be handled by @Transactional annotation when required.
    * User based Read activities like browsing available shows
        * These operations can be quite frequent and are a good candidate for caching and don't have
          too much issues for contention and can be handled by cache + data repositories
    * User based actions like booking of shows, cancellation of shows
        * these are actions can be concurrent specially booking seat or a show. In such a case, we
          can either create a way to lock the seat for the show when the user selects it on the UI.
          For such an activity, we can the following options to achieve the same
            * Handle it on the application by using a HashMap to store which seats have been booked
              by
              which user. Since this makes it application server dependent, this approach will not
              scale with multiple servers
            * Handle it on the database side by creating a lock table. this approach will work with
              multiple application servers however can create a load on the database with high
              concurrent requests. also such actions require auto deletion after specific time
              period
              which will require cron / timestamp column based implementation.
            * Handle it in an in memory store like Redis that can scale as a cluster and is outside
              of
              application and the typical database. hence the application remains stateless and
              there
              is no load on the database for reservation of seats. TTL setting on keys in redis is a
              great feature to complement this requirement for temporarily locks.

Integrate with theatres having existing IT system and new theatres and localization(movies)

* if there are partners with existing IT systems, we can
    * expose RESTful APIs / GraphQL APIs and have their IT system integrate with our APIs
    * Use an integration platform like Mulesoft to adapt their APIs, systems to import data into
      our system
    * if possible set up a message based system to ingest and expose transactional data like booking

How will you scale to multiple cities, countries and guarantee platform availability of 99.99%?

* this will be handled on multiple steps
    * application architecture
        * make sure the application is stateless and cloud native, so it can support horizontal
          scaling
        * create smaller services with loose coupling between them and data processing using
          messaging
        * clustering of database within and across region
    * Infrastructure
        * host application across different AZ in cloud with web proxy on top that can connect a
          user to the nearest AZ
        * application and its components are managed by Kubernetes / managed container service like
          EKS, ECS, so it can scale as required
    * Metrics
        * make the system observable using metrics, traces, logs to monitor availability and faults
          in
          the application
* Development and deployment methodology
    * Development
        * make changes small and incremental with new features with feature flags
        * changes are backward compatible with the last release for a quick rollback
        * implementation of unit and integration test and TDD and BDD
    * Deployment
        * Adopt Blue-Green deployment / Rolling deployment over in place deployment

Integration with payment gateways

* the booking system can send out a message that a new booking is created in Pending State
* we can create a payment microservice that can integrate with the payment service and get ack
  based on the payment request. these can be triggered back to the user once the payment is
  received / rejected / time period expires via messaging within the platform
  How do you monetize platform?
* the platform can be monetized in the following ways
    * convenience fees from end customers for booking movie tickets
    * percentage commission from channel partners when any ticket is booked for one of their
      theatres
    * monthly subscription / on-boarding fees for channel partners
    * white labeling fees / custom implementation for a partner who is looking for custom branding
      / deployment of the solution as per their needs

How to protect against OWASP top 10 threats.

* implement a Web Application Firewall (WAF) on top of web application
* create different public - private - protected subnetworks within the VPC with their own
  access permissions
* implement zero trust policy and every API is appropriately authenticated and authorized for use
* implement rule/ policy based authorization
* have an API gateway abstracting microservices underneath and implementing cross-cutting
  concerns like security
* sensitive data is encrypted at rest
* APIs and UI is encrypted in transit with latest SSL protocols like TLS 1.3
* use code analysis and security testing tools to figure out known vulnerabilities in components
  used in the tech stack.
* check against XSS attacks by using ui frameworks and SQL injection by using jdbc templates and
  parameters, jpa etc
* making sure passwords and user identifiable attributes are not logged, passed to other
  external system