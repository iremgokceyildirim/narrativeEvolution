<%@ page import="nardiff.mt.NardiffStuff; nardiff.mt.InsertInitialStories; nardiff.mt.NarrativeRequest; nardiff.mt.Narrative; nardiff.mt.NarrativeController" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>
<%
    out.println("HelloWorld")
%>
<body>
This script populates the database with new narratives.


<%
    if (request.getParameter("doInit") != null) {
        nardiff.mt.NardiffStuff.insertInitialStories(5,10);

//        Narrative na;
//        na = new Narrative();
//        na.root_narrative_id = 1;
//        na.text = "Once there was a little boy who lived in a hot country.  " +
//                "One day his mother told him to take some cake to his grandmother.  " +
//                "She warned him to hold it carefully so it wouldnt break into " +
//                "crumbs.  The little boy put the cake in a leaf under his arm " +
//                "and carried it to his grandmother's.  \n" +
//                "When he got there the cake had crumbled into tiny pieces. " +
//                "His grandmother told him he was a silly boy and that he " +
//                "should have carried the cake on top of his head so it " +
//                "wouldn't break.  Then she gave him a pat of butter to " +
//                "take back to his mother's house.\n" +
//                "The little boy wanted to be very careful with the butter, " +
//                "so he put it on top of his head and carried it home.  " +
//                "The sun was shining hard and when he got home the butter " +
//                "had all melted.  \n" +
//                "His mother told him that he was a silly boy and that " +
//                "he would have put the butter in a leaf so that it would " +
//                "have gotten home safe and sound.";
//
//        Narrative.withTransaction { tx ->
//            na.save flush: true, failOnError: true
//        }
//
//        NarrativeRequest nr = new NarrativeRequest();
//        nr.parent_narrative = na;
//        nr.root_narrative = na;
//        nr.priority = 1;
//
//        NarrativeRequest.withTransaction { tx ->
//            nr.save flush: true, failOnError:true
//        }
//
//
//
//
//        Narrative n2 = new Narrative();
//        n2.root_narrative_id = 2;
//        n2.text = "Once there was a woman who needed a tiger’s whisker.  She " +
//                "was afraid of tigers, but she needed a whisker to make a " +
//                "medicine for her husband who had gotten very sick.  She thought " +
//                "and thought about how to get a tiger’s whisker.    She decided " +
//                "to use a trick.\n" +
//                "She know that tigers loved food and music.  She thought that if " +
//                "she brought food to a lonely tiger and played soft music, the " +
//                "tiger would be nice to her and she could get the whisker.\n" +
//                "She went to a tiger’s cave where a lonely tiger lived.  She " +
//                "put a bowl of food in front of the opening of the cave.  Then " +
//                "she sang soft music.  The tiger came out and ate the food.  " +
//                "He then walked over to the lady and thanked her for the " +
//                "delicious food and lovely music.  \n" +
//                "The lady then cut off one of his whiskers and ran down the" +
//                " hill very quickly.  The tiger felt lonely and sad again.";
//        nc.save(n2);
//
//        Narrative n3 = new Narrative();
//        n3.root_narrative_id = 3;
//        n3.text = "There was a fox and a bear who were friends.  One day they " +
//
//                "decided to catch a chicken for supper.  They decided to go " +
//                "together, because neither one wanted to be left alone and " +
//                "they both liked fried chicken.\n" +
//                "They waited until night time.  Then they ran very quickly " +
//                "to a nearby farm where they knew chickens lived.  The bear, " +
//                "who felt very lazy climbed up on the roof to watch.  The fox " +
//                "then opened the door of the henhouse very carefully.  He " +
//                "grabbed a chicken and killed it.\n" +
//                "As he was carrying it out the henhouse the weight of the " +
//                "bear on the roof caused the roof to crack.  The fox heard " +
//                "the noise and was frightened but it was too late to run out.  " +
//                "The root and the bear fell in, killing five of the chickens. \n" +
//                "The fix and the bear were trapped in the broken henhouse.  " +
//                "Soon the farmer came out to see what was the matter.";
//        nc.save(n3);
//
//        Narrative n4 = new Narrative();
//        n4.root_narrative_id = 4;
//        n4.text = "Judy is going to have a birthday party.  She is ten years " +
//                "old.  She wants a hammer and a saw for presents.  Then she " +
//                "could make a coat rack and fix her doll house.  She asked " +
//                "her father to get them for her.  \n" +
//                "Her father did not want to get them for her.  He did not " +
//                "think that girls should play with a hammer and a saw.  But " +
//                "he wanted to get her something.  So he bought her a " +
//                "beautiful new dress.  Judy liked the dress, but she still " +
//                "wanted the hammer and the saw.\n" +
//                "Later, she told her grandmother about her wish.  Her " +
//                "grandmother knew that Judy really wanted a hammer and a " +
//                "saw.  She decided to get them for her, because when Judy " +
//                "grows up and becomes a woman she will have to fix things " +
//                "when they break.\n" +
//                "Then her grandmother went out that very day and bought " +
//                "the tools for Judy.  She gave them to Judy that night.  " +
//                "Judy was very happy.  Now she could build things with " +
//                "her hammer and saw.";
//        nc.save(n4);
//
//        Narrative n5 = new Narrative();
//        n5.root_narrative_id = 5;
//        n5.text = "A few weeks ago, health care professionals saw an " +
//                "alarming spike in the number of people seeking medical " +
//                "care after being infected with measles.  Measles is a " +
//                "highly contagious disease that the World Health " +
//                "Organization estimates used to cause ~2.6 million " +
//                "deaths per year globally.\n" +
//                "In the United States, the development and widespread" +
//                " use of vaccines has virtually eradicated the disease, " +
//                "but measles is periodically re-introduced when people " +
//                "travel to parts of the world where it is still prevalent.\n" +
//                "Widespread vaccination protects even those who have " +
//                "not been vaccinated themselves, because people who " +
//                "have been vaccinated cannot infect others.  This “herd " +
//                "immunity” reduces the risk that unvaccinated people " +
//                "will be exposed to the measles virus in the first place.\n" +
//                "Herd immunity is useful, because some people (e.g., " +
//                "very young children) cannot be vaccinated.  " +
//                "Unfortunately, vaccination rates have dropped in " +
//                "some parts of the U.S., allowing the disease to " +
//                "spread there and increasing the risk of a more " +
//                "widespread outbreak.";
//        nc.save(n5);
//



    }
%>
</body>
</html>