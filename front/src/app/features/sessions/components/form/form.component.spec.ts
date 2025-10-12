import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let sessionApiService: SessionApiService;

  const mockedSessionService = { sessionInformation: { admin: true }};
  const mockedActivatedRoute = { snapshot: { paramMap: { get: jest.fn() }}};
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

  beforeEach(async () => {
    jest.restoreAllMocks();
    
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockedSessionService },
        { provide: ActivatedRoute, useValue: mockedActivatedRoute },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to sessions route on init if not admin user', () => {
    mockedSessionService.sessionInformation.admin = false;
    const spiedNavigate = jest.spyOn(router, 'navigate');

    component.ngOnInit();

    expect(spiedNavigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should init form with session data on update', () => {
    const spiedGetUrl = jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');
    TestBed.inject(ActivatedRoute);
    const spiedDetail = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockedSession));
    
    component.ngOnInit();
    fixture.detectChanges();

    expect(spiedGetUrl).toHaveBeenCalled();
    expect(component.onUpdate).toBe(true);
    expect(mockedActivatedRoute.snapshot.paramMap.get).toHaveBeenCalledWith('id');
    expect(spiedDetail).toHaveBeenCalledWith(component['id']);

    const h1Element = fixture.nativeElement.querySelector('h1');
    expect(h1Element.textContent).toContain('Update session');
    
    expect(component.sessionForm?.value.name).toEqual(mockedSession.name);
    expect(component.sessionForm?.value.date).toEqual(mockedSession.date.toISOString().split('T')[0]);
    expect(component.sessionForm?.value.teacher_id).toEqual(mockedSession.teacher_id);
    expect(component.sessionForm?.value.description).toEqual(mockedSession.description);
  });

  it('should create session on submit if not on update', () => {
    component.sessionForm = { value: mockedSession } as any;
    const spiedCreate = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockedSession));
    const spiedExitPage = jest.spyOn(component as any, 'exitPage');
    
    component.submit();

    expect(spiedCreate).toHaveBeenCalledWith(mockedSession);
    expect(spiedExitPage).toHaveBeenCalledWith('Session created !');
  });
});
