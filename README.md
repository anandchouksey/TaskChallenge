# TaskChallenge

## Details

- This application enables a user to create, retrieve, update, delete the tasks.
- User can also trigger execution of a task,  monitor the execution status and cancel the running execution.
- The task can be in the following status - `CREATED`, `IN_EXECUTION`, `COMPLETED`, `CANCELLED`.


### v1 APIs

| API       | HTTP METHOD |                                   URI |
|-----------|:-----------:|--------------------------------------:|
| listTasks  |     GET     |                       `/v1/api/tasks` |
| createTask  |    POST     |                       `/v1/api/tasks` |
| getTask  |     GET     |              `/v1/api/tasks/{taskId}` |
| updateTask  |     PUT     |              `/v1/api/tasks/{taskId}` |
| deleteTask  |   DELETE    |              `/v1/api/tasks/{taskId}` |
| getResult  |   GET    |       `/v1/api/tasks/result/{taskId}` |
| executeTask  |   POST    |       `/v1/api/tasks/execute/{taskId}` |

### v2 APIs

| API       | HTTP METHOD |                                   URI |
|-----------|:-----------:|--------------------------------------:|
| triggerExecution  |    POST     |                       `/v2/api/tasks/execute/{taskId}` |
| cancelExecution  |    POST     |                       `/v2/api/tasks/cancel/{taskId}` |
| getExecutionStatus  |      GET      |                       `/v2/api/tasks/status/{taskId}` |


### Run Application Locally
- Run TaskChallengeApplication via IDE or terminal.

### Cleanup Job
- Scheduled Cron task run on 1st day of every month at 12 AM.
