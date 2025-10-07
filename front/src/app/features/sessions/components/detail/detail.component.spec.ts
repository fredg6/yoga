import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DatePipe, TitleCasePipe } from '@angular/common';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;

  const mockedSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };
  const mockedMatSnackBar = { open: jest.fn() };
  const mockedSession = {
    id: 1,
    name: "session",
    description: "session",
    date: new Date(),
    teacher_id: 1,
    users: [2, 3, 4],
    createdAt: new Date(),
    updatedAt: new Date()
  }
  const mockedTeacher = {
    id: 1,
    lastName: "Teacher",
    firstName: "Margaret",
    createdAt: new Date(),
    updatedAt: new Date()
  }

  beforeEach(async () => {
    jest.restoreAllMocks();
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockedSessionService },
        { provide: MatSnackBar, useValue: mockedMatSnackBar },
        TitleCasePipe,
        DatePipe
      ]
    })
      .compileComponents();
    
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    sessionApiService = TestBed.inject(SessionApiService);
    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockedSession));
    teacherService = TestBed.inject(TeacherService);
    jest.spyOn(teacherService, 'detail').mockReturnValue(of(mockedTeacher));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete session', () => {
    const spiedDelete = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(undefined));
    const router = TestBed.inject(Router);
    const spiedNavigate = jest.spyOn(router, 'navigate');
    
    component.delete();
    
    expect(spiedDelete).toHaveBeenCalledWith(component.sessionId);
    expect(mockedMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(spiedNavigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call sessionApiService.participate', () => {
    const spiedParticipate = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
    const spiedFetchSession = jest.spyOn(component as any, 'fetchSession');

    component.participate();
    
    expect(spiedParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(spiedFetchSession).toHaveBeenCalled();
  });

  it('should call sessionApiService.unParticipate', () => {
    const spiedUnParticipate = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
    const spiedFetchSession = jest.spyOn(component as any, 'fetchSession');
    
    component.unParticipate();
    
    expect(spiedUnParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(spiedFetchSession).toHaveBeenCalled();
  });

  it('should fetch and display session detail correctly', () => {
    const h1Element = fixture.nativeElement.querySelector("h1");
    const titleCasePipe = TestBed.inject(TitleCasePipe);
    expect(h1Element.textContent).toContain(titleCasePipe.transform(component.session!.name));
    
    const spanElements = fixture.nativeElement.querySelectorAll("span");
    expect(spanElements).toHaveLength(4);
    expect(spanElements[1].textContent).toContain(`${mockedTeacher.firstName} ${mockedTeacher.lastName.toUpperCase()}`);
    expect(spanElements[2].textContent).toContain(`${mockedSession.users.length} attendees`);
    const datePipe = TestBed.inject(DatePipe);
    expect(spanElements[3].textContent).toContain(datePipe.transform(mockedSession.date, 'longDate'));
    
    const descriptionDivElement = fixture.nativeElement.querySelector("div.description");
    expect(descriptionDivElement.textContent).toContain(mockedSession.description);
    
    const createdDivElement = fixture.nativeElement.querySelector("div.created");
    expect(createdDivElement.textContent).toContain(datePipe.transform(mockedSession.createdAt, 'longDate'));
    
    const updatedDivElement = fixture.nativeElement.querySelector("div.updated");
    expect(updatedDivElement.textContent).toContain(datePipe.transform(mockedSession.updatedAt, 'longDate'));
  });

  it('should display delete button for admin user', () => {
    const deleteButtonSpanElement = fixture.nativeElement.querySelector("button[color=warn] span");
    expect(deleteButtonSpanElement.textContent).toContain('Delete');
  });

  it('should display participate button for not admin user not participating in session', () => {
    mockedSessionService.sessionInformation.admin = false;
    component.isAdmin = false;
    
    component.ngOnInit();
    fixture.detectChanges();
    
    const participateButtonSpanElement = fixture.nativeElement.querySelector("button[color=primary] span");
    expect(participateButtonSpanElement.textContent).toContain('Participate');
  });
});

