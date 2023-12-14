package ru.xaero.schedulebot.scheduletelegrambot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import ru.xaero.schedulebot.scheduletelegrambot.models.Schedule;
import ru.xaero.schedulebot.scheduletelegrambot.repositories.ScheduleRepository;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(int id) {
        return scheduleRepository.findById(id);
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(int id, Schedule updatedSchedule) {
        if (scheduleRepository.existsById(id)) {
            updatedSchedule.setId(id);
            return scheduleRepository.save(updatedSchedule);
        }
        return null;
    }

    public void deleteSchedule(int id) {
        scheduleRepository.deleteById(id);
    }
    public List<Schedule> getTodaySchedules() {
        List<Schedule> allSchedules = getAllSchedules();
        List<Schedule> todaySchedules = new ArrayList<>();
        LocalDate currentLocalDate = LocalDate.now();

        for (Schedule schedule : allSchedules) {
            if (currentLocalDate.isEqual(schedule.getDate())) {
                todaySchedules.add(schedule);
            }
        }

        return todaySchedules;
    }
    public List<Schedule> getWeekSchedules() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);  // Assuming the week ends on Sunday

        return getAllSchedules().stream()
                .filter(schedule -> schedule.getDate().isAfter(startOfWeek.minusDays(1)) && schedule.getDate().isBefore(endOfWeek.plusDays(1)))
                .collect(Collectors.toList());
    }
    public List<Schedule> getMonthSchedules() {
        List<Schedule> allSchedules = getAllSchedules();
        List<Schedule> monthSchedules = new ArrayList<>();
        LocalDate currentLocalDate = LocalDate.now();

        for (Schedule schedule : allSchedules) {
            if (currentLocalDate.getMonth().equals(schedule.getDate().getMonth())) {
                monthSchedules.add(schedule);
            }
        }

        return monthSchedules;
    }

    public List<Schedule> getTomorrowSchedules(){
        List<Schedule> allSchedules = getAllSchedules();
        List<Schedule> tomorrowSchedules = new ArrayList<>();
        LocalDate currentLocalDate = LocalDate.now().plusDays(1);

        for (Schedule schedule : allSchedules) {
            if (currentLocalDate.isEqual(schedule.getDate())) {
                tomorrowSchedules.add(schedule);
            }
        }

        return tomorrowSchedules;
    }






}

