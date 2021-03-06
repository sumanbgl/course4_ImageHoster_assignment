package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.model.UserProfile;
import ImageHoster.service.ImageService;
import ImageHoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    public static final String passwordError = "Password must contain atleast 1 alphabet, 1 number & 1 special character";

    //This controller method is called when the request pattern is of type 'users/registration'
    //This method declares User type and UserProfile type object
    //Sets the user profile with UserProfile type object
    //Adds User type object to a model and returns 'users/registration.html' file
    @RequestMapping("users/registration")
    public String registration(Model model) {
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        model.addAttribute("User", user);
        return "users/registration";
    }

    //This controller method is called when the request pattern is of type 'users/registration' and also the incoming request is of POST type
    //This method first checks if the password contains atleast 1 alphabet (lowercase or uppercase) and 1 number (0-9) and 1 special character
    //If the password meets all the above requirements, this method calls the business logic and after the user record is persisted in the database, directs to login page
    //If the password does not meet the above condition, this method returns 'users/registration.html' file
    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user, Model model) {
        if(user != null && isPasswordStrong(user.getPassword())) {
            userService.registerUser(user);
            return "redirect:/users/login";
        }else{
            User newUser = new User();
            UserProfile profile = new UserProfile();
            newUser.setProfile(profile);
            model.addAttribute("User", newUser);
            model.addAttribute("passwordTypeError",passwordError);
            return "users/registration";
        }
    }

    //This controller method is called when the request pattern is of type 'users/login'
    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    //This controller method is called when the request pattern is of type 'users/login' and also the incoming request is of POST type
    //The return type of the business logic is changed to User type instead of boolean type. The login() method in the business logic checks whether the user with entered username and password exists in the database and returns the User type object if user with entered username and password exists in the database, else returns null
    //If user with entered username and password exists in the database, add the logged in user in the Http Session and direct to user homepage displaying all the images in the application
    //If user with entered username and password does not exist in the database, redirect to the same login page
    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User existingUser = userService.login(user);
        if (existingUser != null) {
            session.setAttribute("loggeduser", existingUser);
            return "redirect:/images";
        } else {
            return "users/login";
        }
    }

    //This controller method is called when the request pattern is of type 'users/logout' and also the incoming request is of POST type
    //The method receives the Http Session and the Model type object
    //session is invalidated
    //All the images are fetched from the database and added to the model with 'images' as the key
    //'index.html' file is returned showing the landing page of the application and displaying all the images in the application
    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();

        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }

    //Checks if the password contains atleast 1 a-z/A-Z, 1 0-9 and 1 special character
    // returns true if the password satisfies the above rule, false otherwise
    public boolean isPasswordStrong(String password){

        boolean alphabetFound = false;
        boolean numberFound = false;
        boolean specialCharacterFound = false;

        char[] passwordChars = password.toCharArray();

        for(Character c : passwordChars){
            if(Character.isAlphabetic(c)){
                if(!alphabetFound) {
                    alphabetFound = true;
                }
            }else if(Character.isDigit(c)){
                if(!numberFound){
                    numberFound = true;
                }
            }else{
                if(!specialCharacterFound){
                    specialCharacterFound = true;
                }
            }
        }

        return (alphabetFound && numberFound && specialCharacterFound);
    }
}
