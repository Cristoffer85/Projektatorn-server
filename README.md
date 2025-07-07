# Projektatorn - Realtime generator of projects (in computer programming) for you and your friends!

## Description
Server application using Java Spring Boot and RabbitMQ for Projektatorn - an application for generating realtime projects for you and friends  coupled to programming. Using pre-prompted Google Gemini AI-API to generate ideas and send back so you can choose which ideas you want to send to your friends all structured in a little better flow. Chat with your friends to discuss all the while everything is securely E2E-encrypted (nothing can be read by server or in-between) Links to share and start project on Github of course included at end.

## Features
- Generate realtime programming ideas on mainpage
- Create user (password securely encrypted through RSA256bit) email verification required through smtp
- Sign in and update useraccount page with info and custom avatars
- View all current users and their profile information
- Send friend request to user - reciever can either choose to accept or reject
- If atleast two users are friends one of users can send project ideas to friend which receiver in chat can choose 1 of two projects to accept. When one is chosen it can "Accept and Share" with friend and once done it moves to a Project in Progress column inside chatcomponent.
- Owner of project can handle project - and both users can either share to Github and/or LinkedIn for actually starting the repo together in for example Github Projects.

## Installation
1. (For local)

Prerequisites:   
 - Java 
 - Docker (preferably also desktop)
 - RabbitMQ
 - MongoDB

Copy repository to local device and open application in IDE. 
Run initially to generate first docker image (might have to use Docker build) for RabbitMQ service.   
Run it in Docker desktop and now RabbitMq should be running and accessible for UI view at localhost:15672

Open MongoDB and connect to local 27017 to view entries.

Rerun application in IDE, view at http://localhost:8081

2. (for deployed) 
Go to https://www.projektatorn.se

## Usage
Main use is for brainstorming ideas on what projects you - together with your friends can use to make programming ideas - mainly by cooperating together and dusting off teamworking skills, which is a highly good skill working in a company where you are very rarely alone (and also same applies for the rest of the world)   

You can never solve all problems alone hence why teamwork is the dreamwork!

## Credits
Classmates from school, Myself, my Family, mighty duck rubber duck.

## License
🏆 MIT License

## Badges
![badmath](https://img.shields.io/badge/Java-100%25-orange)

## Tests
No tests yet implemented.