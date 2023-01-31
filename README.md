[![License][license]](LICENSE)

[license]: https://img.shields.io/badge/license-MIT-green

# PALUnitTesting

A lightweight unit testing framework, contained in a single class.

## Awards

- Nominated as a group for Peer Support Above and Beyond at the Bath Education Awards 2021
- Highly commended personally for Peer Champion at the Bath Education Awards 2021

[(Bath Education Awards 2021)](https://www.thesubath.com/academicreps/awards/2021/)

## Motivation

This project was created for Peer Assisted Learning [(PAL)](https://www.thesubath.com/peer-support/pal/), a programme run by second year students (known as PAL Leaders) for first years (students), designed to help facilitate discussion about the course content, with [light guidance](https://www.essentialgptrainingbook.com/wp-content/online-resources/03%20Peer%20Assisted%20Learning.pdf) from those who have already done the content.

As part of being a PAL Leader, we found that students benefitted greatly from coding exercises based on content that was covered that week (especially for Java, the primary language being taught in lectures at the time).

Given we ran our PAL sessions during the COVID pandemic, we found it was easiest to create and share these exercises using [repl.it](https://replit.com/), an online IDE that supports most languages. Most importantly, it provided a niche feature that if you were to edit a project created by someone else, it would automatically create a fork for your account. This meant that it was very easy for students to quickly create their own forks of the exercises. 

However, at the time, replit didn't have easy support for automated test units, or the use of libraries such as JUnit. Therefore, I opted to create a lightweight version of a testing framework (contained in a single class file that could be easily copy and pasted into exercises).

This allowed PAL Leaders to create automated test units for students, giving them instant feedback on how they're getting on with the exercise remotely. The syntax is also heavily based on JUnits, meaning that it was a small learning curve for PAL Leaders to use this framework. 

_(This project was also partly created to give me an excuse to dive into how JUnit works...)_

## Usage

Examples of usage of this project can be found in the ```compacted/ExampleTestSuite.java``` and ```compacted/ExampleMain.java``` files. There are quite a few common Assertions provided at the top of ```compacted/PALUnitTestingCompacted.java```, with their functionality (hopefully) being pretty self-explanatory.

The ```expanded/``` directory contains each class in ```compacted/PALUnitTestingCompacted.java``` separated out into different files, for readbility.
