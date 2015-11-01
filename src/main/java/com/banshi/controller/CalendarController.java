package com.banshi.controller;

import com.banshi.controller.vo.EventJson;
import com.banshi.utils.DateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CalendarController {

    @RequestMapping(value = "/myCal.do")
    public ModelAndView showMyCal() throws Exception {
        return new ModelAndView("user/myCal");
    }

    @RequestMapping(value = "/newEvent.do")
    public ModelAndView newEvent() throws Exception {
        return new ModelAndView("user/event");
    }

    @RequestMapping(value = "/saveEvent.do")
    @ResponseBody
    public String saveEvent() throws Exception {
        List<EventJson> eventList = new ArrayList<EventJson>();
        EventJson event = new EventJson();
        event.setTitle("try click here");
        event.setUrl("http://www.baidu.com");
        event.setStart(DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MI));
        event.setEnd(DateUtil.formatDate(DateUtil.addDays(new Date(), 1), DateUtil.YYYY_MM_DD_HH_MI));
        eventList.add(event);

        EventJson event2 = new EventJson();
        event2.setTitle("add event");
        event2.setUrl("http://www.91time.me");
        event2.setStart(DateUtil.formatDate(DateUtil.addDays(new Date(), 3), DateUtil.YYYY_MM_DD_HH_MI));
        event2.setEnd(DateUtil.formatDate(DateUtil.addDays(new Date(), 3), DateUtil.YYYY_MM_DD_HH_MI));
        eventList.add(event2);

        Gson gson = new Gson();
        return gson.toJson(eventList);
    }

    @RequestMapping(value = "/eventList.do")
    @ResponseBody
    public String getEventList() throws Exception {

        List<EventJson> eventList = new ArrayList<EventJson>();
        EventJson event = new EventJson();
        event.setTitle("try click here");
        event.setUrl("http://www.baidu.com");
        event.setStart(DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MI));
        event.setEnd(DateUtil.formatDate(DateUtil.addDays(new Date(), 1), DateUtil.YYYY_MM_DD_HH_MI));
        eventList.add(event);

        Gson gson = new Gson();
        return gson.toJson(eventList);
    }

}

