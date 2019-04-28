import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-manage'
})
export abstract class ManageComponent implements OnInit {

  protected abstract module: string;
  protected abstract routerLink: string;
  protected abstract parent;
  protected abstract content;
  protected abstract parentName;
  protected abstract contentName;
  index: number;

  constructor(
    protected titleService: Title,
    protected route: ActivatedRoute
  ) { }

  async ngOnInit() {
    this.setTitle();
    this.setNames();
    this.load();
  }

  async load() {
    await this.readParams();
    await this.getContent();
    this.getIndex();
  }

  setTitle() {
    this.titleService.setTitle(`Manage ${this.module} | AgileXP`);
  }

  protected abstract setNames();

  protected abstract async readParams();

  protected abstract async getContent();

  getIndex() {
    this.index = this.content.length;
  }


  drop(event: CdkDragDrop<string[]>) {
    const newExercisesArray = Object.assign([], this.content);
    moveItemInArray(this.content, event.previousIndex, event.currentIndex);
    this.reorder(newExercisesArray);
  }

  protected abstract reorder(newExercisesArray);

  protected abstract delete(id);

}
