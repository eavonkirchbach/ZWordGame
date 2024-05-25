import javalib.funworld.WorldScene;
import javalib.worldimages.FontStyle;
import javalib.worldimages.TextImage;
import java.awt.Color;
import tester.Tester;

// represents a list of words
interface ILoWord {

  /*
   * TEMPLATE METHODS: 
   * this.checkAndReduce(String letter) -- ILoWord 
   * this.addToEnd(IWord word) -- ILoWord 
   * this.draw(WorldScene ws) -- WorldScene
   * this.isEmpty() -- boolean
   * this.moveDown() -- ILoWord
   * this.hasWordAtBottom(int) -- boolean
   * this.makeInactive() -- ILoWord
   */

  // if the first letter in a word in a list of word matches the given string character,
  // then removes that first letter of the word, for each word in the list of words
  ILoWord checkAndReduce(String letter);

  // adds the given IWord to the end of the list of words
  ILoWord addToEnd(IWord word);  

  // Draws out the words of the list of words onto the given WorldScene
  WorldScene draw(WorldScene ws);

  // tests if an ILoWord is empty
  boolean isEmpty();

  // moves words down the screen
  ILoWord moveDown();

  // checks if this list has a word at the bottom of the screen
  // the bottom of the screen is given
  boolean hasWordAtBottom(int bottom);

  // makes the first word active 
  ILoWord makeActive();

  // check if a given letter is the first letter of any of the words 
  boolean isAFirstLetter(String letter);

  // makes active empty words inactive
  public ILoWord makeInactive();
}

//represents an empty list of words
class MtLoWord implements ILoWord {

  /*
   * same as interface
   * 
   * TEMPLATE METHODS: 
   * this.checkAndReduce(String letter) -- ILoWord 
   * this.addToEnd(IWord word) -- ILoWord 
   * this.draw(WorldScene ws) -- WorldScene
   * this.isEmpty() -- boolean
   * this.moveDown() -- ILoWord
   * this.hasWordAtBottom(int) -- boolean
   * this.makeInactive() -- ILoWord
   * this.isAFirstLetter() -- boolean
   */

  //moves words down the screen
  public ILoWord moveDown() {
    return this;
  }

  // check if a given letter is the first letter of any of the words 
  public boolean isAFirstLetter(String letter) {
    return false;
  }

  // checks if this list has a word at the bottom of the screen
  // the bottom of the screen is given
  public boolean hasWordAtBottom(int bottom) {
    return false;
  }

  // makes active empty words inactive
  public ILoWord makeInactive() {
    return this;
  }

  // makes active empty words inactive
  public ILoWord makeActive() {
    return this;
  }

  // if the first letter in a word in a list of word matches the given string character,
  // then removes that first letter of the word, for each word in the list of words
  public ILoWord checkAndReduce(String letter) {
    return this;
  }

  // adds the given IWord to the end of the list of words
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(word, this);
  }

  // Draws out the words of the list of words onto the given WorldScene
  public WorldScene draw(WorldScene ws) {
    return ws;
  }

  // tests if an ILoWord is empty
  public boolean isEmpty() {
    return true;
  }
}

// to represent a ConsLoWord
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE 
   * 
   * FIELDS: 
   * this.first -- IWord 
   * this.rest -- ILoword
   * 
   * METHODS: 
   * 
   * this.first.checkAndReduce(String letter) -- ILoWord 
   * this.first.addToEnd(IWord word) -- ILoWord 
   * this.first.draw(WorldScene ws) -- WorldScene
   * this.first.isEmpty() -- boolean
   * this.first.moveDown() -- ILoWord
   * this.first.hasWordAtBottom(int) -- boolean
   * this.first.makeInactive() -- ILoWord
   *
   * 
   * METHODS FOR FIELDS: 
   * this.rest.checkAndReduce(String letter) -- ILoWord 
   * this.rest.addToEnd(IWord word) -- ILoWord 
   * this.rest.draw(WorldScene ws) -- WorldScene
   * this.rest.isEmpty() -- boolean
   * this.rest.moveDown() -- ILoWord
   * this.rest.hasWordAtBottom(int) -- boolean
   * this.rest.makeInactive() -- ILoWord
   * this.rest.isAFirstLetter(String)
   */

  // check if a given letter is the first letter of any of the words 
  public boolean isAFirstLetter(String letter) {
    return this.first.isFirstLetter(letter) || this.rest.isAFirstLetter(letter);
  }

  //moves words down the screen
  public ILoWord moveDown() {
    return new ConsLoWord(this.first.moveDown(), this.rest.moveDown());
  }

  // checks if this list has a word at the bottom of the screen
  // the bottom of the screen is given
  public boolean hasWordAtBottom(int bottom) {
    return this.first.atY(bottom) || this.rest.hasWordAtBottom(bottom);
  }

  // makes active empty words inactive
  public ILoWord makeActive() {
    if (!this.first.isEmpty()) {
      return new ConsLoWord(this.first.convertToActive(), this.rest);
    }
    else {
      return new ConsLoWord(this.first, this.rest.makeActive());
    }
  }

  // if the first letter in a word in a list of word matches the given string character,
  // then removes that first letter of the word, for each word in the list of words
  public ILoWord checkAndReduce(String letter) {
    return new ConsLoWord(this.first.reduce(letter), this.rest.checkAndReduce(letter));
  }

  // adds the given IWord to the end of the list of words
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(this.first, this.rest.addToEnd(word));
  }

  // makes active empty words inactive
  public ILoWord makeInactive() {
    if (this.first.isEmpty()) {
      return new ConsLoWord(this.first.convertToInactive(), this.rest.makeInactive());
    }
    else {
      return new ConsLoWord(this.first, this.rest.makeInactive());
    }
  }

  // draws all of the words in this ILoWord onto the given WorldScene
  public WorldScene draw(WorldScene ws) {
    return this.rest.draw(this.first.drawWord(ws));
  }

  // tests if an ILoWord is empty
  public boolean isEmpty() {
    return false;
  }
}

//represents a word in the ZType game
interface IWord {

  /* TEMPLATE 
   * 
   * METHODS: 
   * 
   * this.reduce(String letter) -- IWord 
   * this.convertToInactive() -- IWord
   * this.isEmpty() -- boolean
   * this.moveDown() -- IWord
   * this.atY(int y) -- boolean
   * this.drawWord(WorldScene ws) -- WorldScene
   */

  // reduces the characters in a word if given string matches first letter
  IWord reduce(String let);

  // convert to inactive
  IWord convertToActive();

  // tests if a word's string value is empty
  boolean isEmpty();

  // move a word down 5px y
  IWord moveDown();

  // checks if a word is at a given y value 
  boolean atY(int y);

  // draws a word on a background, the blue for active words and gray for inactive words 
  WorldScene drawWord(WorldScene ws);

  // checks if a given letter is the first letter of the word
  boolean isFirstLetter(String letter);

  // convert to inactive
  IWord convertToInactive();
}

//represents an active word in the ZType game
class ActiveWord implements IWord {
  String word;
  int x;
  int y;

  ActiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  /*
   * TEMPLATE 
   * 
   * FIELDS:
   * this.word -- String
   * this.x -- int
   * this.y -- int
   * 
   * METHODS: 
   * 
   * this.reduce(String letter) -- IWord 
   * this.convertToInactive() -- IWord
   * this.isEmpty() -- boolean
   * this.moveDown() -- IWord
   * this.atY(int y) -- boolean
   * this.drawWord(WorldScene ws) -- WorldScene
   */

  // checks if a word is at a given y value 
  public boolean atY(int y) {
    return this.y >= y && !this.isEmpty();
  }

  // checks if a given letter is the first letter of the word
  public boolean isFirstLetter(String letter) {
    return this.word.substring(0, 1).equalsIgnoreCase(letter);
  }

  // convert to inactive
  public IWord convertToInactive() {
    return new InactiveWord(this.word, this.x, this.y);
  }

  // convert to inactive
  public IWord convertToActive() {
    return this;
  }

  // checks if a given letter is equal to the first letter of a word, and if it is reduces the word,
  // otherwise returning the original word
  public IWord reduce(String letter) {
    if (!this.isEmpty()) {
      String wordReduced = this.word.substring(1);
      String firstLetter = this.word.substring(0, 1).toLowerCase();
      if (firstLetter.equals(letter.toLowerCase())) {
        return new ActiveWord(wordReduced, x, y);
      }
      else {
        return this;
      }
    }
    else {
      return this;
    }
  }

  // move a word down 5px y
  public IWord moveDown() {
    return new ActiveWord(this.word, this.x, this.y + 5);
  }

  // tests if a word's string value is empty
  public boolean isEmpty() {
    return this.word.equals("");
  }

  // draws an active word 
  public WorldScene drawWord(WorldScene ws) {
    return ws.placeImageXY(new TextImage(this.word, 24, FontStyle.BOLD, Color.blue),
        this.x,
        this.y);
  }
}

// represents an inactive word in the ZType game
class InactiveWord implements IWord {
  String word;
  int x;
  int y;

  InactiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  /*
   * TEMPLATE 
   * 
   * FIELDS:
   * this.word -- String
   * this.x -- int
   * this.y -- int
   * 
   * METHODS: 
   * 
   * this.reduce(String letter) -- IWord 
   * this.convertToInactive() -- IWord
   * this.isEmpty() -- boolean
   * this.moveDown() -- IWord
   * this.atY(int y) -- boolean
   * this.drawWord(WorldScene ws) -- WorldScene
   */

  // checks if a given letter is the first letter of the word
  public boolean isFirstLetter(String letter) {
    return false;
  }

  // draws an inactive word 
  public WorldScene drawWord(WorldScene ws) {
    return ws.placeImageXY(new TextImage(this.word, 24, FontStyle.BOLD, Color.red), this.x, this.y);
  }


  // move a word down 5px y
  public IWord moveDown() {
    return new InactiveWord(this.word, this.x, this.y + 5);
  }

  // convert to active
  public IWord convertToActive() {
    return new ActiveWord(this.word, this.x, this.y);
  }

  // convert to inactive
  public IWord convertToInactive() {
    return this;
  }


  // checks if a word is at a given y value 
  // false for inactive words 
  public boolean atY(int y) {
    return this.y >= y && !this.isEmpty();
  }

  // tests if a word's string value is empty
  public boolean isEmpty() {
    return this.word.equals("");
  }

  // reduces the characters in a word if given string matches first letter
  public IWord reduce(String let) {
    return this;
  }
}

//all examples and tests for ILoWord
class ExamplesWordLists {

  IWord activeEmptyWord = new ActiveWord("", 25, 25);
  IWord inactiveEmptyWord = new InactiveWord("", 35, 35);
  IWord doing = new ActiveWord("doing", 15, 15);
  IWord today = new ActiveWord("today", 15, 30);
  IWord you = new InactiveWord("you", 15, 0);
  IWord hello = new ActiveWord("Hello", 0, 0);

  ILoWord empty = new MtLoWord();
  ILoWord inactive = new ConsLoWord(new InactiveWord("hi", 0, 0), empty);
  ILoWord emptyFirst = new ConsLoWord(new ActiveWord("", 0, 0),
      new ConsLoWord(new ActiveWord("bye", 0, 0), empty));
  ILoWord emptyMix = new ConsLoWord(new InactiveWord("bye", 0, 0),
      new ConsLoWord(new ActiveWord("", 0, 0), empty));
  ILoWord abc0 = new ConsLoWord(new ActiveWord("a", 0, 0),
      new ConsLoWord(new ActiveWord("b", 1, 1), new ConsLoWord(new ActiveWord("c", 2, 2), empty)));
  ILoWord ABC1 = new ConsLoWord(new ActiveWord("A", 0, 0),
      new ConsLoWord(new ActiveWord("B", 1, 1), new ConsLoWord(new ActiveWord("C", 2, 2), empty)));
  ILoWord ABC2 = new ConsLoWord(new InactiveWord("A", 0, 0), new ConsLoWord(
      new InactiveWord("B", 1, 1), new ConsLoWord(new InactiveWord("C", 2, 2), empty)));
  ILoWord fedc0 = new ConsLoWord(new ActiveWord("f", 0, 0), new ConsLoWord(
      new ActiveWord("e", 0, 0),
      new ConsLoWord(new ActiveWord("d", 0, 0), new ConsLoWord(new ActiveWord("c", 0, 0), empty))));
  ILoWord cdef0 = new ConsLoWord(new ActiveWord("c", 0, 0), new ConsLoWord(
      new ActiveWord("d", 0, 0),
      new ConsLoWord(new ActiveWord("e", 0, 0), new ConsLoWord(new ActiveWord("f", 0, 0), empty))));
  ILoWord fedc1 = new ConsLoWord(new InactiveWord("f", 0, 0),
      new ConsLoWord(new InactiveWord("d", 0, 0), new ConsLoWord(new InactiveWord("e", 0, 0),
          new ConsLoWord(new InactiveWord("c", 0, 0), empty))));
  ILoWord cdef1 = new ConsLoWord(new InactiveWord("c", 0, 0),
      new ConsLoWord(new InactiveWord("d", 0, 0), new ConsLoWord(new InactiveWord("e", 0, 0),
          new ConsLoWord(new InactiveWord("f", 0, 0), empty))));
  ILoWord CDEF1 = new ConsLoWord(new ActiveWord("C", 0, 0), new ConsLoWord(
      new ActiveWord("D", 0, 0),
      new ConsLoWord(new ActiveWord("E", 0, 0), new ConsLoWord(new ActiveWord("F", 0, 0), empty))));
  ILoWord helloWorld = new ConsLoWord(new ActiveWord("hello", 0, 0),
      new ConsLoWord(new ActiveWord("world", 0, 0), empty));
  ILoWord goldenGoldFish = new ConsLoWord(new ActiveWord("golden", 0, 0), new ConsLoWord(
      new ActiveWord("gold", 0, 0), new ConsLoWord(new ActiveWord("fish", 0, 0), empty)));
  ILoWord emptyPrincess = new ConsLoWord(new ActiveWord("", 0, 0),
      new ConsLoWord(new ActiveWord("princess", 1, 2), empty));
  ILoWord emptyPrince = new ConsLoWord(new ActiveWord("", 0, 0),
      new ConsLoWord(new InactiveWord("prince", 1, 2), empty));
  ILoWord mix = new ConsLoWord(new ActiveWord("blue", 0, 0),
      new ConsLoWord(new InactiveWord("pink", 0, 0), empty));
  ILoWord caseMix = new ConsLoWord(new ActiveWord("A", 0, 0),
      new ConsLoWord(new ActiveWord("b", 0, 0), empty));

  // test the method convertToInactive for IWords
  boolean testConvertToInactive(Tester t) {
    // tests an active word
    return t.checkExpect(this.doing.convertToInactive(), new InactiveWord("doing", 15, 15))
        // tests an inactive word
        && t.checkExpect(this.inactiveEmptyWord.convertToInactive(), this.inactiveEmptyWord);
  }

  // tests the method atY for IWords
  boolean testAtY(Tester t) {
    // tests an inactive word
    return t.checkExpect(this.inactiveEmptyWord.atY(35), false)
        // tests an active word with y position at given y value
        && t.checkExpect(this.doing.atY(15), true)
        // tests an active word with y position greater than given y value
        && t.checkExpect(this.doing.atY(10), true)
        // tests an active word with y position less than given y value
        && t.checkExpect(this.doing.atY(20), false);
  }

  // tests the method makeInactive for ILoWords
  boolean testMakeInactive(Tester t) {
    // tests an MtLoWord
    return t.checkExpect(this.empty.makeInactive(), this.empty)
        // tests a ConsLoWord with a first word that is an empty string
        && t.checkExpect(this.emptyFirst.makeInactive(), 
            new ConsLoWord(new InactiveWord("", 0, 0),
                new ConsLoWord(new ActiveWord("bye", 0, 0), empty)))
        // tests a ConsLoWord with a first word that is not an empty string
        && t.checkExpect(this.abc0.makeInactive(), this.abc0)
        // tests a ConsLoWord with a non-first word that is an empty string
        && t.checkExpect(this.emptyMix.makeInactive(),
            new ConsLoWord(new InactiveWord("bye", 0, 0),
                new ConsLoWord(new InactiveWord("", 0, 0), empty)));
  }


  // tests the method reduce
  boolean testReduce(Tester t) {
    // reduce an empty word
    return t.checkExpect(this.activeEmptyWord.reduce("t"), this.activeEmptyWord)
        // reduce a non-empty word with a letter that does not match
        && t.checkExpect(this.today.reduce("z"), this.today)
        // reduce a non-empty word with a matching letter
        && t.checkExpect(this.today.reduce("t"), new ActiveWord("oday", 15, 30));
  }

  // tests the method checkAndReduce
  boolean testCheckAndReduce(Tester t) {
    return 
        // tests empty list of words
        t.checkExpect(this.empty.checkAndReduce("a"), this.empty)
        // tests inactive list of words
        && t.checkExpect(this.inactive.checkAndReduce("a"), this.inactive)                       
        // tests inactive list of words w/ correct given letter
        && t.checkExpect(this.inactive.checkAndReduce("h"),
            new ConsLoWord(new InactiveWord("hi", 0, 0), empty))
        // tests active list w/ wrong given letter
        && t.checkExpect(this.helloWorld.checkAndReduce("a"), this.helloWorld)
        // tests active list w/ one reduction
        && t.checkExpect(this.helloWorld.checkAndReduce("h"),
            new ConsLoWord(new ActiveWord("ello", 0, 0),
                new ConsLoWord(new ActiveWord("world", 0, 0), empty)))
        // tests active list with multiple reductions
        && t.checkExpect(this.goldenGoldFish.checkAndReduce("g"),
            new ConsLoWord(new ActiveWord("olden", 0, 0), new ConsLoWord(
                new ActiveWord("old", 0, 0), 
                new ConsLoWord(new ActiveWord("fish", 0, 0), empty))));
  }

  // tests the method isEmpty
  boolean testIsEmpty(Tester t) {
    // check if an empty active IWord is empty
    return t.checkExpect(this.activeEmptyWord.isEmpty(), true)
        // check if an empty inactive word is empty
        && t.checkExpect(this.inactiveEmptyWord.isEmpty(), true)
        // check if a non-empty word is empty
        && t.checkExpect(this.doing.isEmpty(), false)
        // check if a non-empty word is empty
        && t.checkExpect(this.today.isEmpty(), false);
  }


  // tests the method addToEnd
  boolean testAddToEnd(Tester t) {
    return
        // tests empty list of words w/ active given
        t.checkExpect(this.empty.addToEnd(new ActiveWord("hi", 0, 0)),
            new ConsLoWord(new ActiveWord("hi", 0, 0), empty))
        // tests empty list of words w/ inactive given
        && t.checkExpect(this.empty.addToEnd(new InactiveWord("world", 0, 0)),
            new ConsLoWord(new InactiveWord("world", 0, 0), empty))
        // tests inactive list of words w/ active given
        && t.checkExpect(this.inactive.addToEnd(new ActiveWord("erika", 0, 0)), new ConsLoWord(
            new InactiveWord("hi", 0, 0), new ConsLoWord(new ActiveWord("erika", 0, 0), empty)))
        // tests inactive list of words w/ inactive given
        && t.checkExpect(this.inactive.addToEnd(new InactiveWord("fred", 0, 0)), new ConsLoWord(
            new InactiveWord("hi", 0, 0), new ConsLoWord(new InactiveWord("fred", 0, 0), empty)))
        // tests active list of words w/ active given
        && t.checkExpect(this.abc0.addToEnd(new ActiveWord("bye", 1, 0)),
            new ConsLoWord(new ActiveWord("a", 0, 0),
                new ConsLoWord(new ActiveWord("b", 1, 1), new ConsLoWord(new ActiveWord("c", 2, 2),
                    new ConsLoWord(new ActiveWord("bye", 1, 0), empty)))))
        // tests active list of words w/ inactive given
        && t.checkExpect(this.abc0.addToEnd(new InactiveWord("byebye", 2, 3)),
            new ConsLoWord(new ActiveWord("a", 0, 0),
                new ConsLoWord(new ActiveWord("b", 1, 1), new ConsLoWord(new ActiveWord("c", 2, 2),
                    new ConsLoWord(new InactiveWord("byebye", 2, 3), empty)))));
  }

  // tests the method draw
  boolean testDraw(Tester t) {
    return
        // tests empty list of words
        t.checkExpect(this.empty.draw(new WorldScene(500, 500)), new WorldScene(500, 500))
        // tests inactive word
        && t.checkExpect(this.inactive.draw(new WorldScene(300, 200)), new WorldScene(300, 200)
            .placeImageXY(new TextImage("hi", 24, FontStyle.BOLD, Color.red), 0, 0))
        // tests active words
        && t.checkExpect(this.abc0.draw(new WorldScene(100, 100)),
            new WorldScene(100, 100)
            .placeImageXY(new TextImage("a", 24, FontStyle.BOLD, Color.blue), 0, 0)
            .placeImageXY(new TextImage("b", 24, FontStyle.BOLD, Color.blue), 1, 1)
            .placeImageXY(new TextImage("c", 24, FontStyle.BOLD, Color.blue), 2, 2))
        // tests a mix of active and inactive words
        && t.checkExpect(this.mix.draw(new WorldScene(50, 75)),
            new WorldScene(50, 75)
            .placeImageXY(new TextImage("blue", 24, FontStyle.BOLD, Color.blue), 0, 0)
            .placeImageXY(new TextImage("pink", 24, FontStyle.BOLD, Color.red), 0, 0));
  }

  // tests the method drawWord
  boolean testDrawWord(Tester t) {
    // testing drawing an empty word
    return t.checkExpect(this.activeEmptyWord.drawWord(new WorldScene(500, 500)),
        new WorldScene(500, 500).placeImageXY(
            new TextImage("", 24, FontStyle.BOLD, Color.blue), 25, 25))
        // testing drawing an active word
        && t.checkExpect(this.hello.drawWord(new WorldScene(500, 500)),
            new WorldScene(500, 500).placeImageXY(
                new TextImage("Hello", 24, FontStyle.BOLD, Color.blue), 0, 0))
        // testing drawing an inactive word
        && t.checkExpect(this.you.drawWord(new WorldScene(500, 500)),
            new WorldScene(500, 500).placeImageXY(
                new TextImage("you", 24, FontStyle.BOLD, Color.red), 15, 0));
  }


  // tests the method moveDown
  boolean testMoveDown(Tester t) {
    // test a ConsLoWordwith an inactive word
    return t.checkExpect(this.inactive.moveDown(),
        new ConsLoWord(new InactiveWord("hi", 0, 5), empty))
        // tests a ConsLoWord with an active word
        && t.checkExpect(this.abc0.moveDown(),
            new ConsLoWord(new ActiveWord("a", 0, 5),
                new ConsLoWord(new ActiveWord("b", 1, 6), 
                    new ConsLoWord(new ActiveWord("c", 2, 7), empty))))
        // tests a ConsLoWord with a mix of inactive and active words
        && t.checkExpect(this.emptyMix.moveDown(),
            new ConsLoWord(new InactiveWord("bye", 0, 5),
                new ConsLoWord(new ActiveWord("", 0, 5), empty)))
        // tests an MtLoWord
        && t.checkExpect(this.empty.moveDown(), this.empty);
  }

  // tests the method hasWordAtBottom
  boolean testHasWordAtBottom(Tester t) {
    // tests an active word at the bottom of the screen
    return t.checkExpect(
        new ConsLoWord(new ActiveWord("bye", 0, 500), empty).hasWordAtBottom(500), true)
        // tests an active word NOT at the bottom of the screen
        && t.checkExpect(
            new ConsLoWord(new ActiveWord("bye", 0, 200), empty).hasWordAtBottom(500), false)
        // tests an inactive word
        && t.checkExpect(this.inactive.hasWordAtBottom(500), false);
  }

  // test isAFirstLetter method
  boolean testIsAFirstLetter(Tester t) {
    // test on an empty list
    return t.checkExpect(this.empty.isAFirstLetter("a"), false)
        // test on a non-empty list with no match
        && t.checkExpect(this.abc0.isAFirstLetter("z"), false)
        // test on a non-empty list with a match
        && t.checkExpect(this.abc0.isAFirstLetter("a"), true);
  }

  // test isFirstLetter method
  boolean testIsFirstLetter(Tester t) {
    // test on an active word where the first letter is the given letter
    return t.checkExpect(new ActiveWord("bye", 0, 0).isFirstLetter("b"), true)
        // test on an inactive word where the first letter is the given letter
        && t.checkExpect(new InactiveWord("bye", 0, 0).isFirstLetter("b"), false)
        // test on an active word where the given letter is not the first letter
        && t.checkExpect(new ActiveWord("bye", 0, 0).isFirstLetter("a"), false)
        // test on an inactive word where the given letter is not the first letter
        && t.checkExpect(new InactiveWord("bye", 0, 0).isFirstLetter("a"), false);
  }

  // tests makeActive method for ILoWords
  boolean testMakeActive(Tester t) {
    // tests an MtLoWord
    return t.checkExpect(this.empty.makeActive(), this.empty)
        // tests a ConsLoWord with a first word that is inactive
        && t.checkExpect(this.inactive.makeActive(), 
            new ConsLoWord(new ActiveWord("hi", 0, 0), empty))
        // tests a ConsLoWord with a first word that is active 
        && t.checkExpect(this.abc0.makeActive(), this.abc0);
  }

  // tests covertToActive method for IWords
  boolean testConvertToActive(Tester t) {
    // tests an inactive word
    return t.checkExpect(this.you.convertToActive(), new ActiveWord("you", 15, 0))
        // tests an active word
        && t.checkExpect(this.doing.convertToActive(), this.doing);
  }
}
