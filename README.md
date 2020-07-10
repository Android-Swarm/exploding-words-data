# Exploding Words Data Processor

[![Build Status](https://travis-ci.org/Android-Swarm/exploding-words-data.svg?branch=master)](https://travis-ci.org/Android-Swarm/exploding-words-data)
[![Codecov](https://img.shields.io/codecov/c/github/Android-Swarm/exploding-words-data)](https://codecov.io/gh/Android-Swarm/exploding-words-data)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Android-Swarm/exploding-words-data)](https://www.codefactor.io/repository/github/android-swarm/exploding-words-data)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/Android-Swarm/exploding-words-data/issues)

This mini-project is part of the Exploding Words Android application. It is solely responsible for preparing the data to be seeded to the backend database. The data is the list of English words together with the definition and example sentence.

Instead of processing the raw list of English words, this application also allows modification to the list. The allowed operations are the `ADD` operation and the `DELETE` operation. The app also provides the `CHECK` operation to verify the existence of a word in the list, which can ease the modification of the list. Finally, to get the desired data, the application calls the external Urban Dictionary API to get the definition and example; parse them together with the word into a `JSON` structured file (known as the `DUMP` operation). For more details on the design and implementation, check the wiki section of this repository.

The list of words, the definitions, and the example sentences belong to the external party. This application only consumes them for the completion of its purpose. See the credits section below for more details.

## Demonstration
#### Adding a New Word to the Set
<img src="https://github.com/Android-Swarm/exploding-words-data/blob/documentation/ADD.gif" width= "206" height= "283" title="Only words that consist of purely letters can be added to the set" />

#### Removing a Word from the Set
<img src="https://github.com/Android-Swarm/exploding-words-data/blob/documentation/DELETE.gif" width= "206" height= "267" title="Removes the word from the set if it is found" />

#### Checking for a Word's Existence
<img src="https://github.com/Android-Swarm/exploding-words-data/blob/documentation/CHECK.gif" width= "360" height= "283" title="Checks whether the word is already in the set" />

#### Fetching a Word's Definition and Example
<img src="https://github.com/Android-Swarm/exploding-words-data/blob/documentation/DUMP.gif" width= "560" height= "213" title="Checks whether the word is already in the set" />

## Credits
- [This repository](https://github.com/dwyl/english-words) is the source of the initial set of English words
- [Urban Dictionary](https://www.urbandictionary.com/) is the source for the definitions and the example sentences

## Trivia
- This is a personal project for learning purpose
- This project's primary goal is to increase my fluency in coding using the Kotlin programming language
