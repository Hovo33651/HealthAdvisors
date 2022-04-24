package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateTestimonialRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.Rating;
import com.example.healthadvisors.entity.Testimonial;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final RatingService ratingService;
    private final TestimonialService testimonialService;
    private final ModelMapper modelMapper;


    @Value("${health.advisors.patient.pictures.upload.path}")
    String path;


    /**
     * redirects null current user into register page
     */
    @GetMapping("/register")
    public String redirectToRegisterPage() {
        return "register";
    }


    /**
     * accepts data from null current user
     * if any field has not filled correctly, throws error
     * creates entity instances from dto
     * saves entities in database
     * sends activation email to not null, not active current user
     */
    @PostMapping("/register")
    public String registerAsPatient(@ModelAttribute @Valid CreateUserRequest createUserRequest,
                                    BindingResult bindingResult,
                                    @ModelAttribute @Valid CreatePatientRequest createPatientRequest,
                                    @ModelAttribute @Valid CreateAddressRequest createAddressRequest,
                                    @RequestParam("picture") MultipartFile[] uploadedFiles,
                                    ModelMap map,
                                    Locale locale) throws IOException, MessagingException {
        log.info("registerAsPatient: request from {} to create a new account",
                createUserRequest.getEmail());
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError allError : bindingResult.getAllErrors()) {
                errors.add(allError.getDefaultMessage());
            }
            map.addAttribute("errors", errors);
            log.error("registerAsPatient: rejected request to create a new account: email {}. Wrong data!",
                    createUserRequest.getEmail());
            return "register";
        }
        patientService.registerPatient(createUserRequest, createPatientRequest,
                createAddressRequest, uploadedFiles, locale);
        log.info("registerAsPatient: a new non-active account created for {}",
                createUserRequest.getEmail());
        return "redirect:/home";
    }


    /**
     * finds user from database with sent token
     * if null sends a message
     * if active sends a message
     * else sets active true
     * removes user's token
     * removes user's token created date
     * sends message to active user
     */
    @GetMapping("/user/activate")
    public String activateUser(ModelMap map,
                               @RequestParam(name = "token") String token) {
        Optional<User> user = userService.findByToken(token);
        log.info("activateUser: request to activate the account");
        if (!user.isPresent()) {
            log.warn("activateUser: request to activate the account: user doesn't exist");
            map.addAttribute("message", "User does not exists");
            return "activateUser";
        }
        User userFromDb = user.get();
        if (userFromDb.isActive()) {
            log.warn("activateUser: request to activate the account: account is already active, email: {}",
                    userFromDb.getEmail());
            map.addAttribute("message", "Your account is activated");
            return "activateUser";
        }
        userService.activateUser(userFromDb);
        log.info("activateUser: request to activate the account: account has been activated, email: {}",
                userFromDb.getEmail());
        map.addAttribute("message", "Your account has been activated. \n Please sign in");
        return "redirect:/login";
    }


    /**
     * accepts patient's picture url
     * returns picture by byte array
     */
    @GetMapping(value = "/getPatientImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path, picName);
    }


    /**
     * shows current user(patient) the list of doctors using pagination
     */
    @GetMapping("/makeAppointment")
    public String chooseDoctor(ModelMap map,
                               @AuthenticationPrincipal CurrentUser currentUser,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("chooseDoctor: request to choose a doctor for appointment, email: {}",
                currentUser.getUser().getEmail());
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Doctor> allDoctors = doctorService.findAllDoctors(pageRequest);
        map.addAttribute("doctors", allDoctors);

        int totalPages = allDoctors.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
        }
        return "viewAllDoctors";
    }


    /**
     * accepts the doctor id
     * accepts the appointment date and time
     * saves the appointment in database
     */
    @PostMapping("/newAppointment")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  @RequestParam("appointmentDate") String appointmentDate,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("makeAppointment: request to make an appointment, email: {}",
                currentUser.getUser().getEmail());
        appointmentService.newAppointment(doctorId, appointmentDate, currentUser.getUser());
        log.info("chooseDoctor: a new appointment is made, email: {}",
                currentUser.getUser().getEmail());
        return "redirect:/home";
    }


    /**
     * accepts the appointment id
     * removes appointment from database
     */
    @PostMapping("/discardAppointment/{appointmentId}")
    public String discardAppointment(@PathVariable int appointmentId,
                                     @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("discardAppointment: request to remove an appointment, email: {}",
                currentUser.getUser().getEmail());
        appointmentService.deleteAppointmentById(appointmentId);
        log.info("discardAppointment: appointment has been removed, email: {}",
                currentUser.getUser().getEmail());
        return "redirect:/home";
    }


    /**
     * accepts the doctor id
     * finds the doctor by id
     * finds doctor's average rating from database
     */
    @GetMapping("/doctor")
    public String makeAppointment(@AuthenticationPrincipal CurrentUser currentUser,
                                  @RequestParam("doctorId") int doctorId,
                                  ModelMap map) {
        log.info("makeAppointment: request to see doctor's profile, email: {}",
                currentUser.getUser().getEmail());
        Doctor doctorById = doctorService.findDoctorById(doctorId);
        doctorById.setRating(ratingService.getDoctorRating(doctorId));
        log.info("makeAppointment: redirected to /{}/ doctor's page, email: {}",
                doctorById.getUser().getEmail(), currentUser.getUser().getEmail());
        map.addAttribute("doctor", doctorById);
        return "viewDoctorPage";
    }


    /**
     * accepts Principal
     * removes user's profile
     * redirects to /
     */
    @GetMapping("/deleteAccount")
    public String deleteAccount(@AuthenticationPrincipal CurrentUser currentUser) {
        String email  = currentUser.getUser().getEmail();
        log.info("deleteAccount: request to remove the account, email: {}",email);
        userService.deleteUserById(currentUser.getUser().getId());
        log.info("deleteAccount: account {} has been removed", email);
        return "redirect:/";
    }

    /**
     * accepts doctor id and rate
     * creates Rating instance
     * saves in database
     * redirects to the same doctor page
     */
    @PostMapping("/rate")
    public String rate(@RequestParam("doctorId") int doctorId,
                       @RequestParam("rate") int rate) {
        Rating rating = Rating.builder()
                .rating(rate)
                .doctor(doctorService.findDoctorById(doctorId))
                .build();
        ratingService.save(rating);
        return "redirect:/doctor?doctorId=" + doctorId;
    }

    /**
     * redirects to writeTestimonialPage.html
     */
    @GetMapping("/testimonial")
    public String redirectToTestimonialPage() {
        return "writeTestimonialPage";
    }

    /**
     * accepts Principal
     * accepts Testimonial DTO
     * creates Testimonial instance by ModelMapper
     * saves in database
     * redirects to /
     */
    @PostMapping("/testimonial")
    public String addTestimonial(@AuthenticationPrincipal CurrentUser currentUser,
                                 @ModelAttribute CreateTestimonialRequest createTestimonialRequest) {
        log.info("addTestimonial: request to leave a testimonial, email: {}",
                currentUser.getUser().getEmail());
        Testimonial newTestimonial = modelMapper.map(createTestimonialRequest, Testimonial.class);
        newTestimonial.setUser(currentUser.getUser());
        testimonialService.save(newTestimonial);
        log.info("addTestimonial: a new testimonial has been added, email: {}",
                currentUser.getUser().getEmail());
        return "redirect:/";
    }

}