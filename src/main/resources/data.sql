INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('aleksandar.madjarev@gmail.com', MD5('aleksandar.madjarev@gmail.com'), 'madjarica', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zorica@gmail.com', MD5('zorica@gmail.com'), 'zoricab', 'blabla', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov99@gmail.com', MD5('zoranjankov99@gmail.com'), 'zoki', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov991@gmail.com', MD5('zoranjankov991@gmail.com'), 'zoki1', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov992@gmail.com', MD5('zoranjankov992@gmail.com'), 'zoki2', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov993@gmail.com', MD5('zoranjankov993@gmail.com'), 'zoki3', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov994@gmail.com', MD5('zoranjankov994@gmail.com'), 'zoki4', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov995@gmail.com', MD5('zoranjankov995@gmail.com'), 'zoki5', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov996@gmail.com', MD5('zoranjankov996@gmail.com'), 'zoki6', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());
INSERT INTO `onlinemoviedatabase`.`user`(`email`, `hashed_email`, `username`, `password`, `password_temp`, `code_for_activation`, `active`, `status`, `blocked_until`, `subscription`, `created_date`, `updated_date`) VALUES ('zoranjankov997@gmail.com', MD5('zoranjankov997@gmail.com'), 'zoki7', 'password', NULL , NULL , TRUE, FALSE, NULL, TRUE, NOW(), NOW());

INSERT INTO `onlinemoviedatabase`.`role`(`type`) VALUES ('ROLE_USER');
INSERT INTO `onlinemoviedatabase`.`role`(`type`) VALUES ('ROLE_ADMIN');

INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (1, 2);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (2, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (2, 2);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (3, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (3, 2);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (4, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (5, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (6, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (7, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (8, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (9, 1);
INSERT INTO `onlinemoviedatabase`.`user_roles`(`user_id`, `role_id`) VALUES (10, 1);

INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`) VALUES('Movie', 'madjarica', '/nxhfenC1jb5EcTI5GYxqnjYZMId.jpg', NULL, 'http://www.die-fabelhafte-welt-der-amelie.de', 'http://www.imdb.com/title/tt0211915', 'French', 'Le fabuleux destin d''Amélie Poulain', 'At a tiny Parisian café, the adorable yet painfully shy Amélie (Audrey Tautou) accidentally discovers a gift for helping others. Soon Amelie is spending her days as a matchmaker, guardian angel, and all-around do-gooder. But when she bumps into a handsome stranger, will she find the courage to become the star of her very own love story?', '/f0uorE7K7ggHfr8r7pUTOHWkOlE.jpg', 'Amélie', 'https://www.youtube.com/embed/HUECWi5pX7o', NULL, '2001-04-25 02:00:00.000', 0, 122, 194, NULL, NULL, NULL, NULL);
INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`) VALUES('Movie', 'madjarica', '/eWzEX02Pw9TN8nCsCASPdtBHXo6.jpg', NULL, '', 'http://www.imdb.com/title/tt0090756', 'English', 'Blue Velvet', 'The discovery of a severed human ear found in a field leads a young man on an investigation related to a beautiful, mysterious nightclub singer and a group of criminals who have kidnapped her child.', '/psJb2NQKUWDQyhMRV3hoEWk60gS.jpg', 'Blue Velvet', 'https://www.youtube.com/embed/bWr4JvAWF20', NULL, '1986-08-01 02:00:00.000', 0, 120, 793, NULL, NULL, NULL, NULL);
INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`) VALUES('Movie', 'madjarica', '/r2zB2vGLDQVxlRZISH7KzQ6reiC.jpg', NULL, '', 'http://www.imdb.com/title/tt0090605', 'English', 'Aliens', 'When Ripley''s lifepod is found by a salvage crew over 50 years later, she finds that terra-formers are on the very planet they found the alien species. When the company sends a family of colonists out to investigate her story, all contact is lost with the planet and colonists. They enlist Ripley and the colonial marines to return and search for answers.', '/nORMXEkYEbzkU5WkMWMgRDJwjSZ.jpg', 'Aliens', 'https://www.youtube.com/embed/LSHAgmGR-Ig', NULL, '1986-07-18 02:00:00.000', 0, 137, 679, NULL, NULL, NULL, NULL);
INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`) VALUES('TvShow', 'madjarica', '/gX8SYlnL9ZznfZwEH4KJUePBFUM.jpg', NULL, 'http://www.hbo.com/game-of-thrones', 'http://www.imdb.com/title/tt0944947', 'English', 'Game of Thrones', 'Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night''s Watch, is all that stands between the realms of men and icy horrors beyond.', '/gwPSoYUHAKmdyVywgLpKKA4BjRr.jpg', 'Game of Thrones', 'https://www.youtube.com/embed/giYeaKsXnsI', NULL, NULL, NULL, NULL, NULL, '2017-08-27 02:00:00.000', 7, '2011-04-17 02:00:00.000', 1399);
INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`) VALUES('TvShow', 'madjarica', '/qOc3Y92gNfhIXSH59GCIKqkVhSK.jpg', NULL, 'http://stargate.mgm.com/view/series/1/index.html', 'http://www.imdb.com/title/tt0118480', 'English', 'Stargate SG-1', 'The story of Stargate SG-1 begins about a year after the events of the feature film, when the United States government learns that an ancient alien device called the Stargate can access a network of such devices on a multitude of planets. SG-1 is an elite Air Force special operations team, one of more than two dozen teams from Earth who explore the galaxy and defend against alien threats such as the Goa''uld, Replicators, and the Ori.', '/rst5xc4f7v1KiDiQjzDiZqLtBpl.jpg', 'Stargate SG-1', 'https://www.youtube.com/embed/5qfQHEj_f4Y', NULL, NULL, NULL, NULL, NULL, '2007-06-22 02:00:00.000', 10, '1997-07-27 02:00:00.000', 4629);
-- INSERT INTO `video`(`dtype`, `added_by`, `backdrop_path`, `created_date`, `homepage`, `imdb_page`, `original_language`, `original_title`, `overview`, `poster_path`, `title`, `trailer_link`, `updated_date`, `date_of_release`, `released`, `runtime`, `tmdb_movie_id`, `final_air_date`, `number_of_seasons`, `pilot_air_date`, `tmdb_tv_show_id`)

INSERT INTO `watchlists`(`watchlist_user`, `visible_to_others`, `favourite`, `video_id`, `watch_date`) VALUES ('zoricab', true, false, 2, NOW());
INSERT INTO `watchlists`(`watchlist_user`, `visible_to_others`, `favourite`, `video_id`, `watch_date`) VALUES ('zoricab', true, false, 1, NOW());
INSERT INTO `watchlists`(`watchlist_user`, `visible_to_others`, `favourite`, `video_id`, `watch_date`) VALUES ('zoki', true, false, 1, NOW());

INSERT INTO `comments`(`comment_content`, `created_date`, `updated_date`, `watchlist_id`, `comment_user`) VALUES ('Strava film', NOW(), NOW(), 1, 'zoki');
INSERT INTO `comments`(`comment_content`, `created_date`, `updated_date`, `watchlist_id`, `comment_user` ) VALUES ('Ok je', NOW(), NOW(), 2, 'madjarica');

INSERT INTO `schedule_lists`(`watchlist_id`, `scheduled_date_time`, `timer`) VALUES ('1', '2017-08-24 09:02:30', 1);
INSERT INTO `schedule_lists`(`watchlist_id`, `scheduled_date_time`, `timer`) VALUES ('3', '2017-08-24 09:03:30', 2);