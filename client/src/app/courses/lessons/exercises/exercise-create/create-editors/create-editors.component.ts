import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray, FormControl, ControlContainer } from '@angular/forms';
import { MatDialogRef, MatDialog } from '@angular/material';
import { DialogComponent } from '../dialog/dialog.component';
import { Lesson } from '../../../shared/lesson.model';
import { Exercise } from '../../shared/exercise/exercise.model';

@Component({
  selector: 'create-editors',
  templateUrl: './create-editors.component.html',
  styleUrls: ['./create-editors.component.scss']
})
export class CreateEditorsComponent implements OnInit {

  @Input() exerciseFormGroup: FormGroup;
  exerciseType: string;
  exerciseTypePlural: string;
  editorOptions = { theme: 'vs', language: 'java'/*, minimap: {'enabled': false}*/ };
  lessons: Array<Lesson>;
  exercises: Array<Exercise>;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public controlContainer: ControlContainer
  ) { }

  ngOnInit() {
    this.form = <FormGroup>this.controlContainer.control;
    this.setExerciseType();
    this.setFormControls();
  }

  setExerciseType() {
    this.exerciseType = this.form.get('exerciseType').value;
    this.exerciseTypePlural = this.form.get('exerciseTypePlural').value;
  }

  setFormControls() {
    this.form.addControl(
      'hiddenControl', this.fb.group({
        tabContent: this.fb.array([this.create()])
      })
    );
    this.form.addControl(
      'shownType', this.fb.group({
        chosen: ['same', Validators.compose([Validators.required])]
      })
    );
    this.form.addControl(
      'shownControl', this.fb.group({
        tabContent: this.fb.array([this.create()])
      })
    );
    this.setupValidators();
  }

  create(): FormGroup {
    return this.fb.group({
      filename: ['TestFilename.java'],
      content: ['', Validators.compose([Validators.required])]
    });
  }


  setupValidators() {
    this.exerciseFormGroup.get('intro').get('type').valueChanges.subscribe(typeValue => {
      if (typeValue.value.search(this.exerciseType) !== -1) {
        this.form.get('exercise').controls.forEach(control => {
          control.get('content').setValidators(Validators.required);
          control.get('content').updateValueAndValidity();
        });
      } else {
        this.form.get('exercise').controls.forEach(control => {
          control.get('content').clearValidators();
          control.get('content').updateValueAndValidity();
        });
      }
    });
    // this.form.get('shownType').get('chosen').valueChanges.subscribe(chosenValue => {
    //   if (chosenValue === 'custom') {
    //     this.form.get('shown').controls.forEach(control => {
    //       control.get('content').setValidators(Validators.required);
    //       control.get('content').updateValueAndValidity();
    //     });
    //   } else if (chosenValue === 'same') {
    //     this.form.get('shown').controls.forEach(control => {
    //       control.get('content').clearValidators();
    //       control.get('content').updateValueAndValidity();
    //     });
    //   }
    // });
  }

}
