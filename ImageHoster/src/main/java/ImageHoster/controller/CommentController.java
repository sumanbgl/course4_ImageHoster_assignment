package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    //TODO: Revisit the comments
    //This controller method is called when the request pattern is of type '/image/{imageId}/{imageTitle}/comments' and also the incoming request is of POST type
    //The method receives all the details of the comment to be stored in the database, and now the comment will be sent to the business logic to be persisted in the database
    //After you get the comment, set the user of the comment by getting the logged in user from the Http Session
    //Fetch the image using id of the image, from the repository
    //Set the image for the comment
    //Set the date on which the comment is posted
    //After storing the comment, this method directs to the /images/{id}/{title} page, which displays the image details along with all the comments for that image

    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments", method = RequestMethod.POST)
    public String createComment(@RequestParam("comment") String comment, @PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String imageTitle, Comment newComment, HttpSession session){

        User user = (User)session.getAttribute("loggeduser");

        Image existingImage = imageService.getImage(imageId);

        newComment.setText(comment);
        newComment.setCreatedDate(LocalDate.now());
        newComment.setUser(user);
        newComment.setImage(existingImage);

        commentService.createComment(newComment);

        return "redirect:/images/"+existingImage.getId()+"/"+existingImage.getTitle();
    }
}
