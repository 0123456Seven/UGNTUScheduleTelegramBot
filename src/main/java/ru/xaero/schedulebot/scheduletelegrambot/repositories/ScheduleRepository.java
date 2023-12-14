package ru.xaero.schedulebot.scheduletelegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.xaero.schedulebot.scheduletelegrambot.models.Schedule;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}

