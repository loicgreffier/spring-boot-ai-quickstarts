/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.loicgreffier.chat.rag.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.ai.document.Document;

public class RagData {
    public static final List<Document> DOCUMENTS = List.of(
            new Document(
                    UUID.nameUUIDFromBytes("000".getBytes()).toString(),
                    "The Simpsons is an animated sitcom created by Matt Groening that first aired in 1989.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("001".getBytes()).toString(),
                    "Homer Simpson works as a safety inspector at the Springfield Nuclear Power Plant.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("002".getBytes()).toString(),
                    "Marge Simpson is the matriarch of the family, known for her tall blue beehive hairdo.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("003".getBytes()).toString(),
                    "Bart Simpson is the eldest child, a ten-year-old troublemaker who attends Springfield Elementary School.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("004".getBytes()).toString(),
                    "Lisa Simpson is an eight-year-old intellectual who plays the saxophone and advocates for various causes.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("005".getBytes()).toString(),
                    "Maggie Simpson is the baby of the family who communicates by sucking on her pacifier.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("006".getBytes()).toString(),
                    "The family lives at 742 Evergreen Terrace in the fictional town of Springfield.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("007".getBytes()).toString(),
                    "Mr. Burns is Homer's boss and the wealthy, evil owner of the nuclear power plant.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("008".getBytes()).toString(),
                    "Waylon Smithers is Mr. Burns' devoted assistant and the plant's executive.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("009".getBytes()).toString(),
                    "Ned Flanders is the Simpsons' religious, cheerful next-door neighbor.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("010".getBytes()).toString(),
                    "Moe Szyslak runs Moe's Tavern, where Homer and his friends frequently drink.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("011".getBytes()).toString(),
                    "Apu Nahasapeemapetilon operates the Kwik-E-Mart convenience store.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("012".getBytes()).toString(),
                    "Chief Wiggum is Springfield's incompetent police chief.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("013".getBytes()).toString(),
                    "Krusty the Clown hosts a children's television show that Bart idolizes.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("014".getBytes()).toString(),
                    "Sideshow Bob is Krusty's former sidekick who repeatedly tries to kill Bart.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("015".getBytes()).toString(),
                    "Principal Skinner runs Springfield Elementary School alongside Superintendent Chalmers.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("016".getBytes()).toString(),
                    "The show is known for its satirical take on American culture, society, and politics.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("017".getBytes()).toString(),
                    "Each episode typically opens with the family gathering on their couch in different ways.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("018".getBytes()).toString(),
                    "The Simpsons holds the record as the longest-running American animated series.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("019".getBytes()).toString(),
                    "The show features hundreds of recurring characters that make up the town of Springfield.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("020".getBytes()).toString(),
                    "The Simpsons has aired over 750 episodes across more than 35 seasons.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("021".getBytes()).toString(),
                    "Springfield's state location is deliberately kept ambiguous as a running joke throughout the series.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("022".getBytes()).toString(),
                    "The show was developed from animated shorts that appeared on The Tracey Ullman Show from 1987 to 1989.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("023".getBytes()).toString(),
                    "Danny Elfman composed The Simpsons' iconic opening theme music.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("024".getBytes()).toString(),
                    "Springfield contains numerous recurring locations including the nuclear power plant, elementary school, and Moe's Tavern.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("025".getBytes()).toString(),
                    "The Simpsons Movie was released in theaters in 2007 after years of development.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("026".getBytes()).toString(),
                    "Dan Castellaneta provides the voice for Homer Simpson, along with several other characters including Groundskeeper Willie and Krusty the Clown.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("027".getBytes()).toString(),
                    "Julie Kavner voices Marge Simpson and her sisters Patty and Selma Bouvier.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("028".getBytes()).toString(),
                    "Nancy Cartwright is the voice actress behind Bart Simpson, despite being an adult woman.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("029".getBytes()).toString(),
                    "Yeardley Smith voices Lisa Simpson and is the only main cast member who voices just one character.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("030".getBytes()).toString(),
                    "Hank Azaria voices numerous characters including Moe, Apu, Chief Wiggum, and Comic Book Guy.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("031".getBytes()).toString(),
                    "Harry Shearer provides voices for Mr. Burns, Smithers, Ned Flanders, Principal Skinner, and many others.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("032".getBytes()).toString(),
                    "The Simpsons has won 35 Emmy Awards throughout its run, including Outstanding Animated Program.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("033".getBytes()).toString(),
                    "Homer's catchphrase 'D'oh!' was added to the Oxford English Dictionary in 2001.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("034".getBytes()).toString(),
                    "Bart's catchphrases include 'Eat my shorts!' and 'Ay, caramba!'",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("035".getBytes()).toString(),
                    "Santa's Little Helper is the Simpson family's greyhound dog, adopted from a racing track.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("036".getBytes()).toString(),
                    "Snowball II is the family's black cat, named after their first cat Snowball who died.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("037".getBytes()).toString(),
                    "Grampa Simpson, whose full name is Abraham Simpson, is Homer's father and lives at the Springfield Retirement Castle.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("038".getBytes()).toString(),
                    "Patty and Selma Bouvier are Marge's chain-smoking twin sisters who work at the DMV.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("039".getBytes()).toString(),
                    "Barney Gumble is Homer's best friend from high school and a regular at Moe's Tavern.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("040".getBytes()).toString(),
                    "Milhouse Van Houten is Bart's best friend and is known for his unrequited crush on Lisa.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("041".getBytes()).toString(),
                    "Ralph Wiggum is Chief Wiggum's son and a classmate of Lisa's, known for his nonsensical statements.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("042".getBytes()).toString(),
                    "Nelson Muntz is the school bully famous for his mocking 'Ha-ha!' laugh.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("043".getBytes()).toString(),
                    "Edna Krabappel was Bart's teacher at Springfield Elementary, voiced by Marcia Wallace until her death in 2013.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("044".getBytes()).toString(),
                    "Otto Mann is the reckless school bus driver who is a heavy metal enthusiast.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("045".getBytes()).toString(),
                    "Dr. Hibbert is the Simpson family physician, known for his distinctive chuckling laugh.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("046".getBytes()).toString(),
                    "Professor Frink is Springfield's mad scientist character, inspired by Jerry Lewis's Julius Kelp character.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("047".getBytes()).toString(),
                    "The show has featured over 800 guest stars, including celebrities playing themselves and voicing characters.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("048".getBytes()).toString(),
                    "Itchy and Scratchy is the ultraviolent cartoon show within The Simpsons that Bart and Lisa watch.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("049".getBytes()).toString(),
                    "Duff Beer is the fictional brand of beer that Homer and other Springfield residents frequently consume.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("050".getBytes()).toString(),
                    "The Simpsons' opening sequence changes slightly each episode with different chalkboard gags and couch gags.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("051".getBytes()).toString(),
                    "Philippe Peythieu has been the French voice of Homer Simpson since 1990, also voicing Abraham Simpson and Otto Mann.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("052".getBytes()).toString(),
                    "Véronique Augereau provides the French voices for Marge Simpson and her sisters Patty and Selma Bouvier.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("053".getBytes()).toString(),
                    "Philippe Peythieu and Véronique Augereau are married in real life and met while working on The Simpsons dubbing.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("054".getBytes()).toString(),
                    "Aurélia Bruno voices Lisa Simpson and Milhouse Van Houten in the French version since 1990.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("055".getBytes()).toString(),
                    "Joëlle Guigui was the original French voice of Bart Simpson until season 23.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("056".getBytes()).toString(),
                    "Nathalie Bienaimé became the French voice of Bart Simpson from season 23 onwards, replacing Joëlle Guigui.",
                    Map.of()),
            new Document(
                    UUID.nameUUIDFromBytes("057".getBytes()).toString(),
                    "Patrick Guillemin voiced multiple characters in the French version including Ned Flanders, Apu, Barney, and Smithers.",
                    Map.of()));

    private RagData() {}
}
