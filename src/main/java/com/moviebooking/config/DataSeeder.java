package com.moviebooking.config;

import com.moviebooking.model.*;
import com.moviebooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Seed Admin ──
        if (!userRepository.existsByEmail("admin@cinebook.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@cinebook.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Admin created: admin@cinebook.com / admin123");
        }

        // ── Seed Latest Movies ──
        if (movieRepository.count() < 12) {
            movieRepository.deleteAll();
            showRepository.deleteAll();
            seatRepository.deleteAll();

            List<Movie> movies = new ArrayList<>();

            movies.add(createMovie("Dune: Part Two", "Sci-Fi", "English", "2h 46m",
                    "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
                    8.9, "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
                    "2024", true, true, "https://www.youtube.com/embed/Way9Dexny3w",
                    "Timothée Chalamet, Zendaya, Rebecca Ferguson, Javier Bardem"));

            movies.add(createMovie("Kalki 2898 AD", "Sci-Fi Epic", "Telugu", "2h 58m",
                    "A modern avatar of Lord Vishnu, Kalki, is prophesied to descend on Earth to protect humanity from dark forces in Kasi.",
                    8.5, "https://image.tmdb.org/t/p/w500/ugQkpGajKFQ8eyOEhGheR0HfWQ.jpg",
                    "2024", true, true, "https://www.youtube.com/embed/k65eD208FzY",
                    "Prabhas, Amitabh Bachchan, Kamal Haasan, Deepika Padukone"));

            movies.add(createMovie("Deadpool & Wolverine", "Action-Comedy", "English", "2h 08m",
                    "Wolverine is recovering from his injuries when he crosses paths with the loudmouth Deadpool. They team up to defeat a common enemy.",
                    8.8, "https://image.tmdb.org/t/p/w500/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
                    "2024", true, true, "https://www.youtube.com/embed/73_PeQ2prQ0",
                    "Ryan Reynolds, Hugh Jackman, Emma Corrin, Morena Baccarin"));

            movies.add(createMovie("Stree 2", "Horror-Comedy", "Hindi", "2h 21m",
                    "The town of Chanderi is haunted again, but this time by a headless entity named Sarkata. Stree returns to save the town.",
                    8.7,
                    "https://m.media-amazon.com/images/M/MV5BMTA1NmUxYzItZmVmNy00YmQxLTk4Y2UtZjVkMWUwMWQ5N2IxXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                    "2024", true, false, "https://www.youtube.com/embed/S0T8wZ2-5l4",
                    "Shraddha Kapoor, Rajkummar Rao, Pankaj Tripathi, Abhishek Banerjee"));

            movies.add(createMovie("Pushpa 2: The Rule", "Action-Drama", "Telugu", "3h 20m",
                    "Pushpa Raj's dominance in the red sandalwood smuggling trade escalates into a fiercely intense conflict with SP Bhanwar Singh Shekhawat.",
                    8.6, "https://image.tmdb.org/t/p/w500/bhxZj3y59cK7JtGdV285dhDRaMe.jpg",
                    "2024", true, true, "https://www.youtube.com/embed/kYI7-T41q5Y",
                    "Allu Arjun, Rashmika Mandanna, Fahadh Faasil"));

            movies.add(createMovie("Avatar: Fire and Ash", "Sci-Fi", "English", "3h 10m",
                    "Jake Sully and Neytiri travel to new regions of Pandora and encounter the Ash People, a aggressive clan of Na'vi.",
                    9.0, "https://image.tmdb.org/t/p/w500/bRBeSHfGHwkEpImlhxPmOcUsaeg.jpg",
                    "2025", true, true, "https://www.youtube.com/embed/d9MyW72ELq0",
                    "Sam Worthington, Zoe Saldaña, Sigourney Weaver"));

            movies.add(createMovie("Oppenheimer", "Biography-Drama", "English", "3h 00m",
                    "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during WWII.",
                    8.9, "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    "2023", true, false, "https://www.youtube.com/embed/uYPbbksJxIg",
                    "Cillian Murphy, Emily Blunt, Matt Damon, Robert Downey Jr."));

            movies.add(createMovie("Spider-Man: Across the Spider-Verse", "Animation", "English", "2h 20m",
                    "Miles Morales catapults across the Multiverse, encountering a team of Spider-People charged with protecting its existence.",
                    9.1, "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
                    "2024", true, false, "https://www.youtube.com/embed/shW9i6k8cB0",
                    "Shameik Moore, Hailee Steinfeld, Oscar Isaac"));

            movies.add(createMovie("Interstellar", "Sci-Fi", "English", "2h 49m",
                    "When Earth becomes uninhabitable, Joseph Cooper and NASA researchers pilot a spacecraft through a wormhole to find humanity a new home.",
                    8.7, "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                    "2024", true, true, "https://www.youtube.com/embed/zSWdZVtXT7E",
                    "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine"));

            movies.add(createMovie("Bird Box", "Thriller", "English", "2h 04m",
                    "In a world where people are driven to suicide if they see the entity, a woman tries to survive with two children while blindfolded.",
                    8.2, "https://image.tmdb.org/t/p/w500/rGfGfgL2pEPCfhIvqHXieXFn7gp.jpg",
                    "2024", true, false, "https://www.youtube.com/embed/o2AsIXSh2xo",
                    "Sandra Bullock, Trevante Rhodes, John Malkovich"));

            movies.add(createMovie("Migration", "Animation", "English", "1h 23m",
                    "A family of ducks embarks on a thrilling migration from New England to the Caribbean.",
                    8.4, "https://image.tmdb.org/t/p/w500/5qHoazZiaLe7oFBok7XlUhg96f2.jpg",
                    "2024", true, false, "https://www.youtube.com/embed/c0GY8p40zM8",
                    "Kumail Nanjiani, Elizabeth Banks, Awkwafina"));

            movies.add(createMovie("The Batman", "Action-Crime", "English", "2h 56m",
                    "When a sadistic serial killer begins murdering key political figures in Gotham, Batman is forced to investigate the city's hidden corruption.",
                    8.3, "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                    "2024", true, false, "https://www.youtube.com/embed/mqqft2x_Aa4",
                    "Robert Pattinson, Zoë Kravitz, Paul Dano, Colin Farrell"));

            List<Movie> savedMovies = movieRepository.saveAll(movies);

            // ── Seed Shows and Seats for All Movies ──
            String[] halls = { "IMAX 3D", "Dolby Cinema", "Screen 1", "Screen 2", "4DX" };
            LocalTime[] times = {
                    LocalTime.of(10, 30), LocalTime.of(14, 0), LocalTime.of(18, 15), LocalTime.of(21, 45)
            };
            double[] prices = { 350.0, 250.0, 300.0, 400.0 };

            int showIndex = 0;
            for (Movie movie : savedMovies) {
                for (int dayOffset = 0; dayOffset <= 2; dayOffset++) {
                    LocalDate showDate = LocalDate.now().plusDays(dayOffset);
                    for (int t = 0; t < 2; t++) {
                        LocalTime showTime = times[(showIndex + t) % times.length];
                        String hall = halls[(showIndex + t) % halls.length];
                        double price = prices[(showIndex + t) % prices.length];

                        Show show = new Show();
                        show.setMovie(movie);
                        show.setHallName(hall);
                        show.setShowDate(showDate);
                        show.setShowTime(showTime);
                        show.setTotalSeats(30);
                        show.setAvailableSeats(30);
                        show.setTicketPrice(price);
                        Show savedShow = showRepository.save(show);

                        // Seed 30 seats per show (Rows A-F, Seats 1-5)
                        String[] rows = { "A", "B", "C", "D", "E", "F" };
                        for (String r : rows) {
                            for (int i = 1; i <= 5; i++) {
                                Seat seat = new Seat();
                                seat.setShow(savedShow);
                                seat.setSeatNumber(r + i);
                                seat.setSeatType(r.equals("A") || r.equals("B") ? "VIP"
                                        : r.equals("C") || r.equals("D") ? "PREMIUM" : "STANDARD");
                                seat.setBooked(false);
                                seatRepository.save(seat);
                            }
                        }
                        showIndex++;
                    }
                }
            }
            System.out.println("✅ 8 Latest Movies and Shows successfully seeded!");
        }
    }

    private Movie createMovie(String title, String genre, String lang, String duration,
            String desc, double rating, String poster, String releaseDate,
            boolean nowShowing, boolean featured, String trailerUrl, String cast) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setGenre(genre);
        m.setLanguage(lang);
        m.setDuration(duration);
        m.setDescription(desc);
        m.setRating(rating);
        m.setPosterUrl(poster);
        m.setReleaseDate(releaseDate);
        m.setNowShowing(nowShowing);
        m.setFeatured(featured);
        m.setTrailerUrl(trailerUrl);
        m.setCast(cast);
        return m;
    }
}
