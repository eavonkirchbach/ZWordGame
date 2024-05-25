import tester.*;                // The tester library
import java.util.Random;        // Random library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)

// to define our constants in our ZTypeWorld game
interface IZTypeWorld {
  int SCREEN_HEIGHT = 500;
  int SCREEN_WIDTH = 250;
  double TICK_RATE = 0.25;
}

// to represent 
class ZTypeWorld extends World implements IZTypeWorld {
  ILoWord words;
  int numTicks;
  int score;
  int level;
  int seed;

  ZTypeWorld(ILoWord words, int numTicks, int score, int level, int seed) {
    this.words = words;
    this.numTicks = numTicks;
    this.score = score;
    this.level = level;
    this.seed = seed;
  }


  ZTypeWorld(ILoWord words, int numTicks, int score, int level) {
    this(words, numTicks, score, level, -1);
  }

  ZTypeWorld(ILoWord words) {
    this(words, 0, 0, 0, -1);
  }

  /* TEMPLATE
   * FIELDS:
   * ... this.words ...  -- ILoWord
   * ... this.numTicks ... -- int
   * ... this.score ... -- score
   * ... this.level ...  -- int 
   * ... this.seed ...   -- int 
   * 
   * METHODS:
   * ... this.makeScene() ... -- WorldScene
   * ... this.onTick() ... -- World
   * ... this.onKeyEvent(String key)... -- World
   * 
   * FIELD METHODS: 
   * this.words.checkAndReduce(String letter) -- ILoWord 
   * this.words.addToEnd(IWord word) -- ILoWord 
   * this.words.draw(WorldScene ws) -- WorldScene
   * this.words.isEmpty() -- boolean
   * this.words.moveDown() -- ILoWord
   * this.words.
(int) -- boolean
   * this.words.makeInactive() -- ILoWord
   */

  // makes a world scene
  public WorldScene makeScene() {
    if (this.words.hasWordAtBottom(SCREEN_HEIGHT)) {
      return new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT)
          .placeImageXY(new RectangleImage(SCREEN_WIDTH,
              SCREEN_HEIGHT, 
              OutlineMode.SOLID, Color.red), 125, 250)
          .placeImageXY(new TextImage("Game Over", 24, Color.black), 125, 250)
          .placeImageXY(new TextImage("Press r to Restart", 24, Color.black), 125, 350)
          .placeImageXY(new TextImage("Score: " + this.score, 24, Color.black), 125, 450)
          .placeImageXY(new TextImage("Level: " + this.level, 24, Color.black), 125, 480);
    }
    else {
      return this.words.draw(
          new WorldScene(SCREEN_WIDTH, SCREEN_HEIGHT)
          .placeImageXY(new RectangleImage(SCREEN_WIDTH,
              SCREEN_HEIGHT,
              OutlineMode.SOLID,
              Color.lightGray),
              125, 250)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 10)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 150)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 200, 225)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 350)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 450)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 30, 250)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 220, 25)
          .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 230, 400)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 20)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 170)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 220, 245)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 370)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 470)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 50, 270)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 240, 45)
          .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 250, 420));
    }
  }

  // updates the game state every tick
  public World onTick() {
    if (this.numTicks % (20 - level) == 0) {
      IWord newWord = new InactiveWord(new Utils(this.seed).randomLetters(),
          150, 0);
      return new ZTypeWorld(this.words.moveDown().addToEnd(newWord), 
          this.numTicks + 1, this.score, this.level);
    }

    return new ZTypeWorld(this.words.moveDown(), this.numTicks + 1, this.score, this.level);
  }

  // checks key input against the list of words and reduces active words 
  public World onKeyEvent(String key) {
    if (key.equalsIgnoreCase("r") && words.hasWordAtBottom(SCREEN_HEIGHT)) {
      return new ZTypeWorld(new MtLoWord(), 0, 0, 0);
    }

    int newScore = this.score;
    int newLevel = this.level;

    if (this.words.isAFirstLetter(key)) {
      newScore++;
      if (newScore % 10 == 0) {
        newLevel++;
      }
    }
    return new ZTypeWorld(this.words
        .makeActive()
        .makeInactive()
        .checkAndReduce(key)
        .makeActive()
        .makeInactive(), this.numTicks, newScore, newLevel);
  }
}

class Utils {


  /*
   * 
   * TEMPLATE
   * ---------
   * Fields:
   * ... seed ...           -- int
   * ... random ...         -- Random
   */


  int seed;
  Random random;

  Utils(int seed) {
    this.seed = seed;
    if (seed != -1) {
      this.random = new Random(this.seed);
    }
    else {
      this.random = new Random();
    }
  }

  Utils() {
    this(5);
  }

  // generate a string of 6 random letters
  // ACCUMULATOR: current string of random letters
  String randomLettersAcc(String acc) {

    /*
     * Parameters:
     * ... acc ...       -- String
     */

    if (acc.length() < random.nextInt(10) + 1) {
      return randomLettersAcc(acc + (char) (this.random.nextInt(26) + 97));
    }
    return acc;
  }

  //generate a string of 6 random letters
  String randomLetters() {
    return this.randomLettersAcc("");
  }
}

// Examples and tests
class ExamplesWorldScene {
  WorldScene ws0 = new WorldScene(250, 500)
      .placeImageXY(new TextImage("abcdef", 24, FontStyle.BOLD, Color.blue), 0, 0)
      .placeImageXY(new TextImage("babies", 24, FontStyle.BOLD, Color.blue), 1, 1)
      .placeImageXY(new TextImage("babkas", 24, FontStyle.BOLD, Color.blue), 2, 2);
  WorldScene ws1 = new WorldScene(250, 500)
      .placeImageXY(new TextImage("ghsfje", 24, FontStyle.BOLD, Color.blue), 0, 0);
  WorldScene emptyWorld = new WorldScene(250, 500);
  ILoWord empty = new MtLoWord();
  ILoWord abc0 = new ConsLoWord(new ActiveWord("a", 0, 0),
      new ConsLoWord(new ActiveWord("b", 1, 1), new ConsLoWord(new ActiveWord("c", 2, 2), empty)));
  ILoWord ABC1 = new ConsLoWord(new ActiveWord("A", 0, 0),
      new ConsLoWord(new ActiveWord("B", 1, 1), new ConsLoWord(new ActiveWord("C", 2, 2), empty)));
  ILoWord mix = new ConsLoWord(new ActiveWord("blue", 50, 50),
      new ConsLoWord(new InactiveWord("pink", 100, 25), empty));
  ILoWord mixGameOver = new ConsLoWord(new ActiveWord("blue", 50, 1000),
      new ConsLoWord(new InactiveWord("pink", 100, 25), empty));

  // Tests random letters method 
  boolean testRandomLetters(Tester t) {
    return t.checkExpect(new Utils().randomLetters(), "akdd");
  }

  // Tests random letters with accumulator
  boolean testRandomLettersAcc(Tester t) {
    return t.checkExpect(new Utils().randomLettersAcc(""), "akdd")
        && t.checkExpect(new Utils().randomLettersAcc("asd"), "asdakd")
        && t.checkExpect(new Utils().randomLettersAcc("asdfgh"), "asdfgha");
  }

  // Tests makeScene
  boolean testMakeScene(Tester t) {
    return t.checkExpect(new ZTypeWorld(this.abc0).makeScene(),
        new WorldScene(250, 500)
        .placeImageXY(new RectangleImage(250, 500, OutlineMode.SOLID, Color.lightGray), 125, 250)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 10)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 150)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 200, 225)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 350)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 450)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 30, 250)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 220, 25)
        .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 230, 400)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 20)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 170)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 220, 245)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 370)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 470)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 50, 270)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 240, 45)
        .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 250, 420)
        .placeImageXY(new TextImage("a", 24, FontStyle.BOLD, Color.blue), 0, 0)
        .placeImageXY(new TextImage("b", 24, FontStyle.BOLD, Color.blue), 1, 1)
        .placeImageXY(new TextImage("c", 24, FontStyle.BOLD, Color.blue), 2, 2))
        && t.checkExpect(new ZTypeWorld(this.empty).makeScene(),
            new WorldScene(250, 500)
            .placeImageXY(new RectangleImage(250, 500, OutlineMode.SOLID, Color.lightGray),
                125, 250)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 10)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 150)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 200, 225)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 350)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 450)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 30, 250)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 220, 25)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 230, 400)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 20)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 170)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 220, 245)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 370)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 470)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 50, 270)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 240, 45)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 250, 420))
        && t.checkExpect(new ZTypeWorld(this.mix).makeScene(), 
            new WorldScene(250, 500)
            .placeImageXY(new RectangleImage(250, 500, OutlineMode.SOLID, Color.lightGray),
                125, 250)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 10)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 150)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 200, 225)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 100, 350)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 10, 450)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 30, 250)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 220, 25)
            .placeImageXY(new HexagonImage(40, OutlineMode.SOLID, Color.GREEN), 230, 400)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 20)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 170)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 220, 245)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 120, 370)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 20, 470)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 50, 270)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 240, 45)
            .placeImageXY(new HexagonImage(40, OutlineMode.OUTLINE, Color.blue), 250, 420)
            .placeImageXY(new TextImage("blue", 24, FontStyle.BOLD, Color.blue), 50, 50)
            .placeImageXY(new TextImage("pink", 24, FontStyle.BOLD, Color.red), 100, 25))
        && t.checkExpect(new ZTypeWorld(this.mixGameOver).makeScene(), new WorldScene(250, 500)
            .placeImageXY(new RectangleImage(250, 500, OutlineMode.SOLID, Color.red), 125, 250)
            .placeImageXY(new TextImage("Game Over", 24, Color.black), 125, 250)
            .placeImageXY(new TextImage("Press r to Restart", 24, Color.black), 125, 350)
            .placeImageXY(new TextImage("Score: 0", 24, Color.black), 125, 450)
            .placeImageXY(new TextImage("Level: 0", 24, Color.black), 125, 480));
  }

  // initialize BigBang WorldScene
  boolean testBigBang(Tester t) {
    ZTypeWorld w = new ZTypeWorld(this.empty);
    return w.bigBang(250, 500, 0.25);
  }

  // tests the method onTick()
  boolean testOnTick(Tester t) {
    // tests onTick on an empty ILoWord
    return t.checkExpect(new ZTypeWorld(this.empty, 21, 0, 0, 5).onTick(),
        new ZTypeWorld(this.empty, 22, 0, 0))
        // test onTick on a non-empty ILoWord that creates a new word
        && t.checkExpect(
            (new ZTypeWorld(this.abc0, 20, 0, 0, 5)).onTick(),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("a", 0, 5),
                    new ConsLoWord(new ActiveWord("b", 1, 6),
                        new ConsLoWord(new ActiveWord("c", 2, 7),
                            new ConsLoWord(new InactiveWord("akdd", 150, 0), empty)))),
                21, 0, 0, -1))
        // test onTick on a non-empty ILoWord that creates a new word
        && t.checkExpect(
            (new ZTypeWorld(this.abc0, 21, 0, 0)).onTick(),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("a", 0, 5),
                    new ConsLoWord(new ActiveWord("b", 1, 6),
                        new ConsLoWord(new ActiveWord("c", 2, 7), empty))), 22, 0, 0))
        // tests when level is not a multiple of 20
        && t.checkExpect(
            (new ZTypeWorld(this.abc0, 21, 0, 1)).onTick(),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("a", 0, 5),
                    new ConsLoWord(new ActiveWord("b", 1, 6),
                        new ConsLoWord(new ActiveWord("c", 2, 7), empty))), 22, 0, 1))
        // test 
        && t.checkExpect(null, null);
  }

  // tests the method onKeyEvent
  boolean testOnKeyEvent(Tester t) {
    // tests an empty ILoWord
    return t.checkExpect((new ZTypeWorld(this.empty, 0, 0, 0)).onKeyEvent("a"),
        new ZTypeWorld(this.empty, 0, 0,0))
        // tests a non-empty ILoWord with correctly reducing character passed
        && t.checkExpect((new ZTypeWorld(this.mix, 0, 0, 0)).onKeyEvent("b"),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("lue", 50, 50),
                    new ConsLoWord(new InactiveWord("pink", 100, 25), empty)), 0, 1, 0))
        // tests a non-empty ILoWord with incorrectly reducing character passed
        && t.checkExpect((new ZTypeWorld(this.mix, 0, 0, 0)).onKeyEvent("m"),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("blue", 50, 50),
                    new ConsLoWord(new InactiveWord("pink", 100, 25), empty)), 0, 0, 0))
        // tests a non-empty ILoWord that converts an empty string to an inactive word
        && t.checkExpect((new ZTypeWorld(this.abc0, 0, 0, 0)).onKeyEvent("a"),
            new ZTypeWorld(
                new ConsLoWord(new InactiveWord("", 0, 0),
                    new ConsLoWord(new ActiveWord("b", 1, 1),
                        new ConsLoWord(new ActiveWord("c", 2, 2), empty))), 0, 1, 0))
        // tests a non-empty ILoWord that does not converts an empty string to an inactive word
        && t.checkExpect((new ZTypeWorld(this.abc0, 0, 0, 0)).onKeyEvent("f"),
            new ZTypeWorld(
                new ConsLoWord(new ActiveWord("a", 0, 0),
                    new ConsLoWord(new ActiveWord("b", 1, 1),
                        new ConsLoWord(new ActiveWord("c", 2, 2), empty))), 0, 0, 0));
  }
}