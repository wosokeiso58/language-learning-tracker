# Language Dashboard

A JavaFX application for tracking language learning activities.

## Features


### Logging hours

Describe activity and select one of: Speaking, Writing, Reading, Watching content, Anki grinding, Other learning which fall under four categories of the chart: Input, Output, Learning, Grinding

### Tracking progress
A chart should take the current progressed logged and formulate a visual indicator of where abouts the user's current level is in their language. Should take into account breaks which hinder progress exponentially with break length.

- ### Track study sessions
- Visualise study balance
- Generate statistics
- Display graphs

## Future Ideas

get statistics for individual activity types

history graph(s)  for xp

icon in the top right with current XP, level etc

have to select a language to begin with and hold multiple session managers and stuff eventually


- ###

S+ spoken convo 2.25

S:CI without subsCI with subs anki 2

A:shadowing written convo text prepared speech 1.75

B:Read text 1.5

C:textbook 1.25

D:content with subs 1

E:content without subs script practice 0.75

package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
static void main() throws IOException {

}