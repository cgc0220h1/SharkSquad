package com.concamap.controllers.home;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Controller
@RequestMapping("/date")
@PropertySource("classpath:config/homepage.properties")
public class DateController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    private final LocalDateTime endTime = LocalDateTime.now();

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final PostService postService;

    private final PostComponent postComponent;

    @Autowired
    public DateController(PostService postService, PostComponent postComponent) {
        this.postService = postService;
        this.postComponent = postComponent;
    }

    @GetMapping("/{year}")
    public ModelAndView showPostWithinYear(@PathVariable("year") String year, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("filter");
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        LocalDate localDate = LocalDate.parse(year, formatter);
        return getModelAndView(pageable, modelAndView, localDate);
    }

    @GetMapping("/{year}/{month}")
    public ModelAndView showPostWithinDate(@PathVariable("year") String year,
                                           @PathVariable("month") String month,
                                           Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("filter");
        YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        LocalDate localDate = yearMonth.atDay(1);
        return getModelAndView(pageable, modelAndView, localDate);
    }

    private ModelAndView getModelAndView(Pageable pageable, ModelAndView modelAndView, LocalDate localDate) {
        Timestamp startTime = Timestamp.valueOf(localDate.atStartOfDay());
        Page<Post> postPage = postService.findExistWithinTime(startTime, Timestamp.valueOf(endTime), pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }
}
