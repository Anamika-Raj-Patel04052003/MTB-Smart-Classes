package MTB.Smart.Classes.API.service;

import MTB.Smart.Classes.API.dto.AuthResponse;
import MTB.Smart.Classes.API.dto.LoginRequest;
import MTB.Smart.Classes.API.dto.RegisterRequest;
import MTB.Smart.Classes.API.entity.StudentProfile;
import MTB.Smart.Classes.API.entity.User;
import MTB.Smart.Classes.API.entity.UserRole;
import MTB.Smart.Classes.API.repository.StudentProfileRepository;
import MTB.Smart.Classes.API.repository.UserRepository;
import MTB.Smart.Classes.API.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            StudentProfileRepository studentProfileRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User registerStudent(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        String mobile = request.getMobile()
                .trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        if (userRepository.existsByMobile(mobile)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered");
        }

        User user = new User();

        user.setFullName(
                request.getFullName().trim());

        user.setEmail(email);

        user.setMobile(mobile);

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(UserRole.STUDENT);

        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        StudentProfile studentProfile = new StudentProfile();

        studentProfile.setUser(savedUser);

        studentProfile.setParentName(
                request.getParentName().trim());

        studentProfile.setParentMobile(
                request.getParentMobile().trim());

        studentProfile.setClassName(
                request.getClassName().trim());

        if (request.getSchoolName() != null
                && !request.getSchoolName().isBlank()) {

            studentProfile.setSchoolName(
                    request.getSchoolName().trim());
        }

        if (request.getAddress() != null
                && !request.getAddress().isBlank()) {

            studentProfile.setAddress(
                    request.getAddress().trim());
        }

        studentProfileRepository.save(
                studentProfile);

        return savedUser;
    }

    public AuthResponse login(LoginRequest request) {

        String identifier = request.getIdentifier()
                .trim();

        User user = userRepository
                .findByEmail(identifier.toLowerCase())
                .or(() -> userRepository.findByMobile(identifier))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid email/mobile or password"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException(
                    "Your account is disabled");
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash());

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email/mobile or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name());

        return new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful");
    }
}